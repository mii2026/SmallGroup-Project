package com.server.smallgroup;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.UserGroup;
import com.server.smallgroup.Entity.Users;
import com.server.smallgroup.Repository.GroupRepository;
import com.server.smallgroup.Repository.UserGroupRepository;
import com.server.smallgroup.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserGroupRepositoryTest {
    @Autowired
    private UserGroupRepository userGroupRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void beforeEach(){
        this.userGroupRepository.truncateTable();
    }

    @Test
    void testInsert(){
        Optional<Users> ou = userRepository.findById(2L);
        Optional<Groups> og = groupRepository.findById(1L);
        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug);
    }

    @Test
    void testDeleteByUser(){
        Optional<Users> ou = userRepository.findById(2L);
        Optional<Groups> og = groupRepository.findById(1L);
        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug);

        List<UserGroup> list = this.userGroupRepository.findAll();
        assertEquals(1, list.size());

        this.userGroupRepository.deleteByUser(ou.get());
        list = this.userGroupRepository.findAll();
        assertEquals(0, list.size());
    }

    @Test
    void testDeleteByGroup(){
        Optional<Users> ou = userRepository.findById(2L);
        Optional<Groups> og = groupRepository.findById(1L);
        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug);

        List<UserGroup> list = this.userGroupRepository.findAll();
        assertEquals(1, list.size());

        this.userGroupRepository.deleteByGroup(og.get());
        list = this.userGroupRepository.findAll();
        assertEquals(0, list.size());
    }
    @Test
    void testDeleteByGroups(){
        Optional<Users> ou = userRepository.findById(2L);
        Optional<Groups> og = groupRepository.findById(1L);
        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug);

        Optional<Users> ou2 = userRepository.findById(2L);
        Optional<Groups> og2 = groupRepository.findById(2L);
        UserGroup ug2 = new UserGroup(ou2.get(), og2.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug2);

        UserGroup ug3 = new UserGroup(ou.get(), og2.get(), false, LocalDateTime.now());
        this.userGroupRepository.save(ug3);

        List<UserGroup> list = this.userGroupRepository.findAll();
        assertEquals(3, list.size());

        List<Groups> glist = new ArrayList<>();
        glist.add(og.get());
        glist.add(og2.get());
        this.userGroupRepository.deleteByGroups(glist);
        list = this.userGroupRepository.findAll();
        assertEquals(0, list.size());
    }


    @Test
    void testDeleteByGroupAndUser(){
        Optional<Users> ou = userRepository.findById(2L);
        Optional<Groups> og = groupRepository.findById(1L);
        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug);

        List<UserGroup> list = this.userGroupRepository.findAll();
        assertEquals(1, list.size());

        this.userGroupRepository.deleteByGroupAndUser(og.get(), ou.get());
        list = this.userGroupRepository.findAll();
        assertEquals(0, list.size());
    }

//    @Test
//    void testFindByUser(){
//        Optional<Users> ou = userRepository.findById(2L);
//        Optional<Groups> og = groupRepository.findById(1L);
//        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
//        this.userGroupRepository.save(ug);
//
//        List<UserGroup> list = this.userGroupRepository.findByUser(ou.get());
//        assertEquals(1, list.size());
//        assertEquals(1, list.get(0).getId());
//        assertEquals(1L, list.get(0).getGroup().getId());
//    }

    @Test
    void testFindByGroup(){
        Optional<Users> ou = userRepository.findById(2L);
        Optional<Groups> og = groupRepository.findById(1L);
        UserGroup ug = new UserGroup(ou.get(), og.get(), true, LocalDateTime.now());
        this.userGroupRepository.save(ug);

        List<UserGroup> list = this.userGroupRepository.findByGroup(og.get());
        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getId());
        assertEquals(2L, list.get(0).getUser().getId());
    }
}
