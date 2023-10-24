package com.server.smallgroup.Service;

import com.server.smallgroup.DTO.UserInfoDTO;
import com.server.smallgroup.Entity.*;
import com.server.smallgroup.ImageUtil;
import com.server.smallgroup.MessageUtil;
import com.server.smallgroup.Repository.*;
import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final HobbyRepository hobbyRepository;
    private final UserGroupRepository userGroupRepository;
    private final GroupRepository groupRepository;
    private final GroupHobbyRepository groupHobbyRepository;
    private final KeywordRepository keywordRepository;
    private final MessageUtil messageUtil;

    public boolean checkID(String userId){
        return this.userRepository.findByUserId(userId).isPresent();
    }

    public boolean checkPhoneNum(String phoneNum){
        return this.userRepository.findByPhoneNum(phoneNum).isPresent();
    }

    public String sendVerificationCode(String num){
        final String chars = "0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 6; i++){
            int idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }

        SingleMessageSentResponse response = this.messageUtil.sendVerificationCode(num, sb.toString());
        if(response.getStatusCode().equals("2000")){
            return sb.toString();
        }else {
            return null;
        }
    }

    public Optional<Users> findId(String name, String phoneNum){
        Optional<Users> ou = this.userRepository.findByNameAndPhoneNum(name, phoneNum);
        return ou;
    }

    @Transactional
    public boolean findPw(String name, String id, String phoneNum){
        Optional<Users> ou = this.userRepository.findByNameAndUserIdAndPhoneNum(name, id, phoneNum);
        if(ou.isPresent()){
            Users u = ou.get();
            String temp_pw = makePw(10);
            u.setPw(temp_pw);
            this.userRepository.save(u);
            this.messageUtil.sendPW(u.getPhoneNum(), temp_pw);
            return true;
        }else
            return false;
    }

    public static String makePw(int len){
        final String chars = "0123456789abcdefghijklmnopqrstuvwxyz!@";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        int idx = 0;
        do {
            idx = random.nextInt(chars.length());
        } while (idx > 35);
        sb.append(chars.charAt(idx));

        for(int i = 1; i < len; i++){
            idx = random.nextInt(chars.length());
            sb.append(chars.charAt(idx));
        }

        return sb.toString();
    }

    public Optional<Users> signIn(String userId, String pw){
        Optional<Users> ou = this.userRepository.findByUserIdAndPw(userId, pw);
        return ou;
    }

    public UserInfoDTO getUserInfo(Long id){
        Optional<Users> ou = this.userRepository.findById(id);
        if(ou.isPresent()){
            return new UserInfoDTO(ou.get(), this.userHobbyRepository.findByUser(ou.get()));
        }else{
            return null;
        }
    }

    public void signUp(String userId, String pw, String phoneNum, String name, boolean gender, int age, String address){
        Users u1 = new Users(userId, pw, phoneNum, name, gender, age, address);
        this.userRepository.save(u1);
    }

    @Transactional
    public void changeUserInfo(Long id, String address, String hobby, String message){
        Users u = this.userRepository.findById(id).get();
        if(address!=null)
            u.setAddress(address);
        u.setMessage(message);
        this.userRepository.save(u);

        this.userHobbyRepository.deleteByUser(u);
        List<Integer> ids = new ArrayList<>();
        for(int i = 0; i < hobby.length(); i++){
            if(hobby.charAt(i) == '1')
                ids.add(i+1);
        }
        List<Hobby> hlist = this.hobbyRepository.findAllById(ids);
        List<UserHobby> uhlist = new ArrayList<>();
        for(int i = 0; i < hlist.size(); i++){
            uhlist.add(new UserHobby(u, hlist.get(i)));
        }
        this.userHobbyRepository.saveAll(uhlist);
    }

    public boolean changePw(Long id, String old_pw, String new_pw){
        Optional<Users> ou = this.userRepository.findById(id);
        Users u = ou.get();
        if(!u.getPw().equals(old_pw))
            return false;
        u.setPw(new_pw);
        this.userRepository.save(u);
        return true;
    }

    @Transactional
    public void deleteUser(Long id) throws IOException {
        Optional<Users> ou = this.userRepository.findById(id);
        Users u = ou.get();
        if (u.getPhotoPath() != null)
            Files.delete(Path.of(u.getPhotoPath()));
        this.userRepository.delete(u);
        this.userHobbyRepository.deleteByUser(u);
        List<UserGroup> captain = this.userGroupRepository.findByUserAndIsCaptain(u, true);
        List<Groups> captaingroups =  captain.stream().map(i->i.getGroup()).collect(Collectors.toList());
        this.userGroupRepository.deleteByUser(u);
        this.userGroupRepository.deleteByGroups(captaingroups);
        this.groupHobbyRepository.deleteByGroups(captaingroups);
        this.keywordRepository.deleteByGroups(captaingroups);
        this.groupRepository.deleteByIds(captaingroups.stream().map(i->i.getId()).collect(Collectors.toList()));
    }

    public String getUserImage(Long id){
        Optional<Users> ou = this.userRepository.findById(id);
        return ou.get().getPhotoPath();
    }

    @Transactional
    public void changeUserImage(Long id, MultipartFile file) throws IOException {
        String path = ImageUtil.saveImage(id, "D:\\Project\\smallgroup\\smallgroup\\src\\main\\resources\\image\\profile",file);
        Optional<Users> ou = this.userRepository.findById(id);
        Users u = ou.get();
        u.setPhotoPath(path);
        this.userRepository.save(u);
    }

    @Transactional
    public void deleteUserImage(Long id) throws IOException {
        Optional<Users> ou = this.userRepository.findById(id);
        Users u = ou.get();
        String path = u.getPhotoPath();
        if(path!=null) {
            Files.delete(Path.of(path));
            u.setPhotoPath(null);
            this.userRepository.save(u);
        }
    }
}
