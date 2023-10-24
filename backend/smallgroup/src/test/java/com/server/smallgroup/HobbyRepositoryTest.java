package com.server.smallgroup;

import com.server.smallgroup.Entity.Hobby;
import com.server.smallgroup.Repository.HobbyRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HobbyRepositoryTest {
    @Autowired
    private HobbyRepository hobbyRepository;

    @Test
    public void TestInsertAndFindAll(){
        String[] list = {"강아지", "독서", "피아노", "자기계발", "게임", "보드게임", "운동", "고양이", "헬스", "필라테스", "요가", "재테그"};
        for(String str: list){
            Hobby h = new Hobby(str);
            this.hobbyRepository.save(h);
        }
        List<Hobby> hlist = this.hobbyRepository.findAll();
        assertEquals(12, hlist.size());
        assertEquals("독서", hlist.get(1).getHobby());
    }

    @Test
    public void TestFindByIds(){
        List<Integer> ids = new ArrayList<>();
        ids.add(1);
        ids.add(3);
        ids.add(5);
        List<Hobby> hlist = this.hobbyRepository.findAllById(ids);
        assertEquals(3, hlist.size());
        assertEquals("강아지", hlist.get(0).getHobby());
        assertEquals("피아노", hlist.get(1).getHobby());
        assertEquals("게임", hlist.get(2).getHobby());
    }
}
