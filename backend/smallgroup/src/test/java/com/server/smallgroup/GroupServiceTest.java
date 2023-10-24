package com.server.smallgroup;

import com.server.smallgroup.Entity.GroupHobby;
import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.Keyword;
import com.server.smallgroup.Entity.UserGroup;
import com.server.smallgroup.Repository.*;
import com.server.smallgroup.Service.GroupService;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class GroupServiceTest {
    GroupService groupService;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserGroupRepository userGroupRepository;
    @Autowired
    GroupHobbyRepository groupHobbyRepository;
    @Autowired
    KeywordRepository keywordRepository;

    @Autowired
    HobbyRepository hobbyRepository;

    @BeforeEach
    public void beforeEach(){
        this.groupHobbyRepository.truncateTable();
        this.groupRepository.truncateTable();
        this.keywordRepository.truncateTable();
        this.userGroupRepository.truncateTable();
        this.groupService = new GroupService(groupRepository, userRepository, userGroupRepository, groupHobbyRepository, keywordRepository, hobbyRepository);
    }

    @Test
    public void 그룹생성테스트() throws IOException {
        this.groupService.insertGroup(2L, "화나면 걷는 모임", "000000011000", 20, 30, 10,
                "경기도 수원시 장안구 율전동", "안녕하세요.\n", "#산책#걷기", null);
        List<Groups> glist = this.groupRepository.findAll();
        assertEquals(1, glist.size());

        List<GroupHobby> ghlist = this.groupHobbyRepository.findAll();
        assertEquals(2, ghlist.size());

        List<Keyword> klist = this.keywordRepository.findAll();
        assertEquals(2, klist.size());

        List<UserGroup> uglist = this.userGroupRepository.findAll();
        assertEquals(1, uglist.size());
    }

    @Test
    public void 그룹변경테스트() throws IOException {
        this.groupService.insertGroup(2L, "화나면 걷는 모임", "000000011000", 20, 30, 10,
                "경기도 수원시 장안구 율전동", "안녕하세요.\n", "#산책#걷기", null);


        this.groupService.revisionGroup(1L, "화나면 걷는 모임", "000000001000", 20, 30, 10,
                "경기도 수원시 장안구 율전동", "안녕하세요.\n", "#산책#걷기#힐링", null);

        List<Groups> glist = this.groupRepository.findAll();
        assertEquals(1, glist.size());

        List<GroupHobby> ghlist = this.groupHobbyRepository.findAll();
        assertEquals(1, ghlist.size());

        List<Keyword> klist = this.keywordRepository.findAll();
        assertEquals(3, klist.size());

        List<UserGroup> uglist = this.userGroupRepository.findAll();
        assertEquals(1, uglist.size());
    }

    @Test
    public void 그룹삭제테스트() throws Exception {
        this.groupService.insertGroup(2L, "화나면 걷는 모임", "000000011000", 20, 30, 10,
                "경기도 수원시 장안구 율전동", "안녕하세요.\n", "#산책#걷기", null);

        this.groupService.deleteGroup(1L, 2L);
        List<Groups> glist = this.groupRepository.findAll();
        assertEquals(0, glist.size());

        List<GroupHobby> ghlist = this.groupHobbyRepository.findAll();
        assertEquals(0, ghlist.size());

        List<Keyword> klist = this.keywordRepository.findAll();
        assertEquals(0, klist.size());

        List<UserGroup> uglist = this.userGroupRepository.findAll();
        assertEquals(0, uglist.size());
    }

    @Test
    public void 그룹가입탈퇴테스트() throws Exception {
        this.groupService.insertGroup(2L, "화나면 걷는 모임", "000000011000", 20, 30, 10,
                "경기도 수원시 장안구 율전동", "안녕하세요.\n", "#산책#걷기", null);

        this.groupService.registerGroup(1L, 1L);
        List<UserGroup> uglist = this.userGroupRepository.findAll();
        assertEquals(2, uglist.size());
        assertFalse(uglist.get(1).getIsCaptain());

        this.groupService.leaveGroup(1L, 1L);
        uglist = this.userGroupRepository.findAll();
        assertEquals(1, uglist.size());
    }
}
