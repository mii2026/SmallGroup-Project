package com.server.smallgroup;

import com.server.smallgroup.Entity.Users;
import com.server.smallgroup.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @BeforeEach010
    public void beforeEach(){
        this.userRepository.truncateTable();
    }

    @Test
    void testInsert(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Users u2 = new Users("mii2025", "asdf1231", "01011111111",
                "김철수", false, 25, "경기도 수원시 장안구 천천동");
        this.userRepository.save(u2);
    }

    @Test
    void testFindAll(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Users u2 = new Users("mii2025", "asdf1231", "01011111111",
                "홍길동", false, 24, "경기도 수원시 장안구 천천동");
        this.userRepository.save(u2);

        List<Users> all = this.userRepository.findAll();
        assertEquals(2, all.size());

        Users q = all.get(0);
        assertEquals("mii2026", q.getUserId());
    }

    @Test
    void testFindById(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findById(Long.valueOf(1));
        assertTrue(ou.isPresent());
        Users u = ou.get();
        assertEquals("mii2026", u.getUserId());

        ou = this.userRepository.findById(Long.valueOf(2));
        assertFalse(ou.isPresent());
    }

    @Test
    void testFindByUserId(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findByUserId("mii2026");
        assertTrue(ou.isPresent());
        Users u = ou.get();
        assertEquals("홍길동", u.getName());

        ou = this.userRepository.findByUserId("mii2027");
        assertFalse(ou.isPresent());
    }

    @Test
    void testFindByIdAndPw(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findByUserIdAndPw("mii2026", "asdf1231");
        assertTrue(ou.isPresent());
        Users u = ou.get();
        assertEquals("홍길동", u.getName());

        ou = this.userRepository.findByUserIdAndPw("mii2027", "asdf1231");
        assertFalse(ou.isPresent());

        ou = this.userRepository.findByUserIdAndPw("mii2026", "asdf1234");
        assertFalse(ou.isPresent());

        ou = this.userRepository.findByUserIdAndPw("mii2027", "asdf1234");
        assertFalse(ou.isPresent());
    }

    @Test
    void testFindByNameAndPhoneNum(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findByNameAndPhoneNum("홍길동", "01000000000");
        assertTrue(ou.isPresent());
        Users u = ou.get();
        assertEquals("mii2026", u.getUserId());

        ou = this.userRepository.findByNameAndPhoneNum("홍길동", "01011111111");
        assertFalse(ou.isPresent());

        ou = this.userRepository.findByNameAndPhoneNum("김철수", "01000000000");
        assertFalse(ou.isPresent());

        ou = this.userRepository.findByNameAndPhoneNum("김철수", "01011111111");
        assertFalse(ou.isPresent());
    }

    @Test
    void testFindByNameAndUserIdAndPhoneNum(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findByNameAndUserIdAndPhoneNum("홍길동", "mii2026", "01000000000");
        assertTrue(ou.isPresent());
        Users u = ou.get();
        assertEquals("홍길동", u.getName());

        ou = this.userRepository.findByNameAndUserIdAndPhoneNum("홍길동", "mii2026", "01011111111");
        assertFalse(ou.isPresent());
    }

    @Test
    void testFindByPhoneNum(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findByPhoneNum("01000000000");
        assertTrue(ou.isPresent());
        Users u = ou.get();
        assertEquals("홍길동", u.getName());

        ou = this.userRepository.findByPhoneNum("01011111111");
        assertFalse(ou.isPresent());
    }

    @Test
    void testRevision(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        Optional<Users> ou = this.userRepository.findById(Long.valueOf(1));
        assertTrue(ou.isPresent());
        Users u = ou.get();
        u.setMessage("안녕하세요");
        this.userRepository.save(u);

        ou = this.userRepository.findById(Long.valueOf(1));
        assertTrue(ou.isPresent());
        u = ou.get();
        assertEquals("안녕하세요", u.getMessage());
    }

    @Test
    void testDelete(){
        Users u1 = new Users("mii2026", "asdf1231", "01000000000",
                "홍길동", false, 24, "경기도 수원시 장안구 율전동");
        this.userRepository.save(u1);

        assertEquals(1, this.userRepository.count());
        Optional<Users> ou = this.userRepository.findById(Long.valueOf(1));
        assertTrue(ou.isPresent());
        Users q = ou.get();
        this.userRepository.delete(q);
        assertEquals(0, this.userRepository.count());
    }
}
