package com.server.smallgroup;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GroupRepositoryTest {
    @Autowired
    GroupRepository groupRepository;

    @BeforeEach
    public void beforeEach(){
        this.groupRepository.truncateTable();
    }

    @Test
    public void testInsertGroup(){
        Groups g1 = new Groups("화나면 걷는 모임", 20, 30, 20, 1, "경기도 수원시 장안구 율전동",
                "안녕하세요. 화나면 걷는 모임 방장 홍길동입니다!\n\n본 모임은 친목보다는 평점심을 다스리기 위한 목적의 산책을 지향합니다.",
                LocalDateTime.now());
        this.groupRepository.save(g1);
        Groups g2 = new Groups("화나면 걷는 모임", 20, 30, 20, 1, "경기도 수원시 장안구 율전동",
                "안녕하세요. 화나면 걷는 모임 방장 홍길동입니다!\n\n본 모임은 친목보다는 평점심을 다스리기 위한 목적의 산책을 지향합니다.",
                LocalDateTime.now());
        this.groupRepository.save(g2);
    }

    @Test
    void testRandomGroups(){
        Groups g1 = new Groups("화나면 걷는 모임", 20, 30, 20, 1, "경기도 수원시 장안구 율전동",
                "안녕하세요. 화나면 걷는 모임 방장 홍길동입니다!\n\n본 모임은 친목보다는 평점심을 다스리기 위한 목적의 산책을 지향합니다.",
                LocalDateTime.now());
        this.groupRepository.save(g1);
        Groups g2 = new Groups("화나면 걷는 모임", 20, 30, 20, 1, "경기도 수원시 장안구 율전동",
                "안녕하세요. 화나면 걷는 모임 방장 홍길동입니다!\n\n본 모임은 친목보다는 평점심을 다스리기 위한 목적의 산책을 지향합니다.",
                LocalDateTime.now());
        this.groupRepository.save(g2);

        List<Groups> list = this.groupRepository.randomGroups(2);
        assertEquals(2, list.size());
    }
}
