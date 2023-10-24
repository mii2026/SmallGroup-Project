package com.server.smallgroup;

import com.server.smallgroup.DTO.UserInfoDTO;
import com.server.smallgroup.Entity.Users;
import com.server.smallgroup.Repository.*;
import com.server.smallgroup.Service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserHobbyRepository userHobbyRepository;
    @Autowired
    UserGroupRepository userGroupRepository;
    @Autowired
    HobbyRepository hobbyRepository;
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    GroupHobbyRepository groupHobbyRepository;
    @Autowired
    KeywordRepository keywordRepository;
    @Autowired
    MessageUtil messageUtil;
    UserService userService;

    @BeforeEach
    public void beforeEach(){
        this.userRepository.truncateTable();
        this.userService = new UserService(this.userRepository, this.userHobbyRepository, this.hobbyRepository,
                this.userGroupRepository, this.groupRepository, this.groupHobbyRepository, this.keywordRepository, this.messageUtil);
    }

    @Test
    public void 회원가입(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        Optional<Users> ou = this.userRepository.findById(1L);
        assertTrue(ou.isPresent());
        assertEquals("홍길동", ou.get().getName());
    }

    @Test
    public void 로그인(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");

        Optional<Users> ou = this.userService.signIn("mii2026", "asdf1231");
        assertTrue(ou.isPresent());
        assertEquals(1L, ou.get().getId());

        ou = this.userService.signIn("mii2027", "asdf1231");
        assertFalse(ou.isPresent());
    }

    @Test
    public void 아이디중복확인(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        assertTrue(this.userService.checkID("mii2026"));
        assertFalse(this.userService.checkID("mii2022"));
    }

    @Test
    public void 비밀번호중복확인(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        assertTrue(this.userService.checkPhoneNum("01000000000"));
        assertFalse(this.userService.checkPhoneNum("01011111111"));
    }

    @Test
    public void 아이디찾기(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        Optional<Users> ou = this.userService.findId("홍길동", "01000000000");
        assertTrue(ou.isPresent());
        String id = ou.get().getUserId();
        assertEquals("mii2026", id);

        ou = this.userService.findId("홍길동", "01011111111");
        assertFalse(ou.isPresent());
    }

    @Test
    public void 비밀번호찾기(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");

        assertTrue(this.userService.findPw("홍길동", "mii2026", "01000000000"));
        assertFalse(this.userService.findPw("홍길동", "mii2026", "01011111111"));
    }

    @Test
    public void 비밀번호생성(){
        log.info(UserService.makePw(10));
    }

    @Test
    public void 회원정보검색(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        UserInfoDTO u = this.userService.getUserInfo(1L);
        assertTrue(u!=null);
        assertEquals("mii2026", u.getId());

        assertFalse(this.userService.getUserInfo(10L)!=null);
    }

    @Test
    public void 회원정보변경(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        this.userService.changeUserInfo(1L, "전라북도 전주시 완산구 삼천동", "010011000000", "천천히 걷기");

        UserInfoDTO u = this.userService.getUserInfo(1L);
        assertTrue(u!=null);
        assertEquals("천천히 걷기", u.getMessage());
    }

    @Test
    public void 비밀번호변경(){
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        assertTrue(this.userService.changePw(1L, "asdf1231", "asdf1234"));
        assertFalse(this.userService.changePw(1L, "asdf1231", "asdf1234"));

        UserInfoDTO u = this.userService.getUserInfo(1L);
        assertTrue(u!=null);
    }

    @Test
    public void 회원정보삭제() throws IOException {
        this.userService.signUp("mii2026", "asdf1231", "01000000000", "홍길동", true, 24, "경기도 수원시 장안구 율전동");
        this.userService.signUp("mii2025", "asdf1234", "01011111111", "김철수", false, 25, "경기도 수원시 장안구 천천동");

        List<Users> all = this.userRepository.findAll();
        assertEquals(2, all.size());
        assertTrue(this.userRepository.findById(1L).isPresent());

        this.userService.deleteUser(1L);
        all = this.userRepository.findAll();
        assertEquals(1, all.size());
        assertFalse(this.userRepository.findById(1L).isPresent());
    }
}
