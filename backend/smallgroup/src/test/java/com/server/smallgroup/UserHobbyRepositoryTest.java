package com.server.smallgroup;

import com.server.smallgroup.Entity.UserHobby;
import com.server.smallgroup.Entity.Users;
import com.server.smallgroup.Repository.HobbyRepository;
import com.server.smallgroup.Repository.UserHobbyRepository;
import com.server.smallgroup.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserHobbyRepositoryTest {
    @Autowired
    private UserHobbyRepository userHobbyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HobbyRepository hobbyRepository;

    @BeforeEach
    public void beforeEach(){
        this.userHobbyRepository.truncateTable();
    }

    @Test
    public void testInsertHobby(){
        Optional<Users> u = this.userRepository.findById(1L);
        for(int i = 0; i < 5; i+=2) {
            UserHobby uh = new UserHobby(u.get(), this.hobbyRepository.findById(i+1).get());
            this.userHobbyRepository.save(uh);
        }
    }

    @Test
    public void testDeleteByUser(){
        Optional<Users> u1 = this.userRepository.findById(1L);
        for(int i = 0; i < 5; i+=2) {
            UserHobby uh = new UserHobby(u1.get(), this.hobbyRepository.findById(i+1).get());
            this.userHobbyRepository.save(uh);
        }
        Optional<Users> u2 = this.userRepository.findById(2L);
        for(int i = 1; i < 6; i+=2) {
            UserHobby uh = new UserHobby(u2.get(), this.hobbyRepository.findById(i+1).get());
            this.userHobbyRepository.save(uh);
        }

        List<UserHobby> all = this.userHobbyRepository.findAll();
        assertEquals(6, all.size());

        this.userHobbyRepository.deleteByUser(u1.get());
        all = this.userHobbyRepository.findAll();
        assertEquals(3, all.size());
    }

    @Test
    public void testFindById(){
        Optional<Users> u1 = this.userRepository.findById(1L);
        for(int i = 0; i < 5; i+=2) {
            UserHobby uh = new UserHobby(u1.get(), this.hobbyRepository.findById(i+1).get());
            this.userHobbyRepository.save(uh);
        }
        Optional<Users> u2 = this.userRepository.findById(2L);
        for(int i = 1; i < 6; i+=2) {
            UserHobby uh = new UserHobby(u2.get(), this.hobbyRepository.findById(i+1).get());
            this.userHobbyRepository.save(uh);
        }
        List<UserHobby> list = this.userHobbyRepository.findByUser(u1.get());
        assertEquals(3, list.size());
        assertEquals(1, list.get(0).getHobby().getId());
    }
}
