package com.server.smallgroup;

import com.server.smallgroup.Entity.GroupHobby;
import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Repository.GroupHobbyRepository;
import com.server.smallgroup.Repository.GroupRepository;
import com.server.smallgroup.Repository.HobbyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GroupHobbyRepositoryTest {
    @Autowired
    private GroupHobbyRepository groupHobbyRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private HobbyRepository hobbyRepository;

    @BeforeEach
    public void beforeEach(){
        this.groupHobbyRepository.truncateTable();
    }

    @Test
    public void testInsertHobby(){
        Optional<Groups> g = this.groupRepository.findById(1L);
        for(int i = 0; i < 5; i++) {
            GroupHobby gh = new GroupHobby(g.get(), this.hobbyRepository.findById(i+1).get());
            this.groupHobbyRepository.save(gh);
        }
    }

    @Test
    public void testDeleteByGroup(){
        Optional<Groups> g = this.groupRepository.findById(1L);
        for(int i = 0; i < 5; i++) {
            GroupHobby uh = new GroupHobby(g.get(), this.hobbyRepository.findById(i+1).get());
            this.groupHobbyRepository.save(uh);
        }
        List<GroupHobby> all = this.groupHobbyRepository.findAll();
        assertEquals(5, all.size());

        this.groupHobbyRepository.deleteByGroup(g.get());
        all = this.groupHobbyRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    public void testFindByGroup(){
        Optional<Groups> u = this.groupRepository.findById(1L);
        for(int i = 0; i < 5; i++) {
            GroupHobby uh = new GroupHobby(u.get(), this.hobbyRepository.findById(i+1).get());
            this.groupHobbyRepository.save(uh);
        }
        List<GroupHobby> list = this.groupHobbyRepository.findByGroup(u.get());
        assertEquals(5, list.size());
        assertEquals(1, list.get(0).getHobby().getId());
    }
}
