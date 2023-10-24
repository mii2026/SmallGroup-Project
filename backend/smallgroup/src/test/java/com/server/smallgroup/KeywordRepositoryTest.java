package com.server.smallgroup;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.Keyword;
import com.server.smallgroup.Repository.GroupRepository;
import com.server.smallgroup.Repository.KeywordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class KeywordRepositoryTest {
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void beforeEach(){
        this.keywordRepository.truncateTable();
    }

    @Test
    public void testInsertHobby(){
        Optional<Groups> g = this.groupRepository.findById(1L);
        for(int i = 0; i < 5; i++) {
            Keyword gh = new Keyword(g.get(), String.valueOf(i));
            this.keywordRepository.save(gh);
        }
    }

    @Test
    public void testDeleteByUser(){
        Optional<Groups> g = this.groupRepository.findById(1L);
        for(int i = 0; i < 5; i++) {
            Keyword gh = new Keyword(g.get(), String.valueOf(i));
            this.keywordRepository.save(gh);
        }
        List<Keyword> all = this.keywordRepository.findAll();
        assertEquals(5, all.size());

        this.keywordRepository.deleteByGroup(g.get());
        all = this.keywordRepository.findAll();
        assertEquals(0, all.size());
    }

    @Test
    public void testFindByGroup(){
        Optional<Groups> g = this.groupRepository.findById(1L);
        for(int i = 0; i < 5; i++) {
            Keyword gh = new Keyword(g.get(), String.valueOf(i));
            this.keywordRepository.save(gh);
        }
        List<Keyword> list = this.keywordRepository.findByGroup(g.get());
        assertEquals(5, list.size());
        assertEquals("0", list.get(0).getKeyword());

        g = this.groupRepository.findById(2L);
        list = this.keywordRepository.findByGroup(g.get());
        assertEquals(0, list.size());
    }
}
