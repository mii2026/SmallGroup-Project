package com.server.smallgroup.Service;

import com.server.smallgroup.DTO.FilterInfoDTO;
import com.server.smallgroup.DTO.GroupInfoDTO;
import com.server.smallgroup.DTO.SimpleGroupInfoDTO;
import com.server.smallgroup.DTO.FindGroupDTO;
import com.server.smallgroup.Entity.*;
import com.server.smallgroup.ImageUtil;
import com.server.smallgroup.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupHobbyRepository groupHobbyRepository;
    private final KeywordRepository keywordRepository;
    private final HobbyRepository hobbyRepository;

    public GroupInfoDTO getGroupInfo(Long id){
        Optional<Groups> og = this.groupRepository.findById(id);
        Groups g = og.get();
        List<GroupHobby> hobby_list = this.groupHobbyRepository.findByGroup(g);
        List<Users> user_list = this.userGroupRepository.findByGroup(g).stream().map(i->i.getUser()).collect(Collectors.toList());
        return new GroupInfoDTO(g, hobby_list, user_list);
    }

    @Transactional
    public void insertGroup(Long user_id, String name, String hobby, int minAge, int maxAge, int admit, String address,
                            String message, String keyword, MultipartFile image) throws IOException {
        LocalDateTime createTime = LocalDateTime.now();
        Groups g = new Groups(name, minAge, maxAge, admit, 1, address, message, createTime);
        if(image!=null && !image.isEmpty()){
            String path = ImageUtil.saveImage(g.getId(), "D:\\프로젝트\\Project\\smallgroup\\smallgroup\\src\\main\\resources\\static\\image\\group", image);
            g.setImagePath(path);
        }
        this.groupRepository.save(g);

        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < hobby.length(); i++){
            if(hobby.charAt(i)=='1')
                ids.add(i+1);
        }
        List<Hobby> hlist = this.hobbyRepository.findAllById(ids);
        List<GroupHobby> ghlist = new ArrayList<>();
        for(int i = 0; i < hlist.size(); i++){
            ghlist.add(new GroupHobby(g, hlist.get(i)));
        }
        this.groupHobbyRepository.saveAll(ghlist);

        List<Keyword> klist = new ArrayList<>();
        String[] keywords = keyword.substring(1).split("#");
        for(String str: keywords)
            klist.add(new Keyword(g, str));
        this.keywordRepository.saveAll(klist);

        Users u = this.userRepository.findById(user_id).get();
        this.userGroupRepository.save(new UserGroup(u, g, true, createTime));
    }

    @Transactional
    public void revisionGroup(Long group_id, String name, String hobby, int minAge, int maxAge, int admit,
                              String address, String message, String keyword, MultipartFile image) throws IOException {
        Groups g = this.groupRepository.findById(group_id).get();
        if(g.getImagePath()!=null) {
            Files.delete(Path.of(g.getImagePath()));
        }
        if(image!=null && !image.isEmpty()){
            String path = ImageUtil.saveImage(g.getId(), "D:\\프로젝트\\Project\\smallgroup\\smallgroup\\src\\main\\resources\\static\\image\\group", image);
            g.setImagePath(path);
        }else{
            g.setImagePath(null);
        }
        g.setContributes(name, minAge, maxAge, admit, address, message);
        this.groupRepository.save(g);

        this.groupHobbyRepository.deleteByGroup(g);
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < hobby.length(); i++){
            if(hobby.charAt(i)=='1')
                ids.add(i+1);
        }
        List<Hobby> hlist = this.hobbyRepository.findAllById(ids);
        List<GroupHobby> ghlist = new ArrayList<>();
        for(int i = 0; i < hlist.size(); i++){
            ghlist.add(new GroupHobby(g, hlist.get(i)));
        }
        this.groupHobbyRepository.saveAll(ghlist);

        this.keywordRepository.deleteByGroup(g);
        List<Keyword> klist = new ArrayList<>();
        String[] keywords = keyword.substring(1).split("#");
        for(String str: keywords)
            klist.add(new Keyword(g, str));
        this.keywordRepository.saveAll(klist);
    }

    @Transactional
    public void deleteGroup(Long group_id, Long user_id) throws Exception {
        Groups g = this.groupRepository.findById(group_id).get();
        Users u = this.userRepository.findById(user_id).get();

        UserGroup gh = this.userGroupRepository.findByGroupAndUser(g, u).get();
        if(!gh.getIsCaptain())
            throw new Exception("모임장만 삭제 가능합니다.");

        this.groupHobbyRepository.deleteByGroup(g);
        this.keywordRepository.deleteByGroup(g);
        this.userGroupRepository.deleteByGroup(g);
        this.groupRepository.delete(g);
    }

    public void registerGroup(Long group_id, Long user_id) throws Exception {
        Groups g = this.groupRepository.findById(group_id).get();
        Users u = this.userRepository.findById(user_id).get();

        Optional<UserGroup> ogh = this.userGroupRepository.findByGroupAndUser(g, u);
        if(ogh.isPresent())
            throw new Exception("이미 가입한 그룹입니다.");

        if(u.getAge()<g.getMinAge() || u.getAge()>g.getMaxAge())
            throw new Exception("나이가 안맞습니다.");

        if(g.getNumPerson()==g.getAdmit())
            throw new Exception("정원이 초과되었습니다.");

        this.userGroupRepository.save(new UserGroup(u, g, false, LocalDateTime.now()));
    }

    public void leaveGroup(Long group_id, Long user_id) throws Exception {
        Groups g = this.groupRepository.findById(group_id).get();
        Users u = this.userRepository.findById(user_id).get();
        UserGroup gh = this.userGroupRepository.findByGroupAndUser(g, u).get();
        if(gh.getIsCaptain())
            throw new Exception("모임장은 탈퇴할 수 없습니다.");
        this.userGroupRepository.deleteByGroupAndUser(g, u);
    }

    public String getImage(Long group_id){
        Groups g = this.groupRepository.findById(group_id).get();
        return g.getImagePath();
    }

    public Page<SimpleGroupInfoDTO> myGroups(Long user_id, int page){
        Users u = this.userRepository.findById(user_id).get();
        Pageable pageable = PageRequest.of(page, 4);
        Page<UserGroup> uglist = this.userGroupRepository.findByUser(u, pageable);
        return uglist.map(i->new SimpleGroupInfoDTO(i));
    }

    public List<FindGroupDTO> randomGroups(int num){
        List<Groups> glist = this.groupRepository.randomGroups(num);
        List<FindGroupDTO> list = new ArrayList<>();
        for(int i = 0; i < glist.size(); i++){
            list.add(new FindGroupDTO(glist.get(i)));
        }
        return list;
    }

    public Page<FindGroupDTO> findGroup(String keyword, FilterInfoDTO info, Long user_id) throws Exception {
        Pageable pageable = PageRequest.of(info.getPagenum(), 4);
        String address = "%";
        if(user_id!=null){
            address = this.userRepository.findById(user_id).get().getAddress();
            address = address.split(" ")[0]+"%";
        }
        if(info.getOrder().equals("numPerson"))
                return this.groupRepository
                        .findByKeywordOrderByNumPerson(info.getMinage(), info.getMaxage(),address,pageable)
                        .map(i->new FindGroupDTO(i));
        else if(info.getOrder().equals("createTime"))
            return this.groupRepository
                    .findByKeywordOrderByCreateTime(info.getMinage(), info.getMaxage(),address,pageable)
                    .map(i->new FindGroupDTO(i));
        else
            throw new Exception("없는 순서");

    }
}
