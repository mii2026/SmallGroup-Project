package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Modifying @Transactional
    @Query(value = "delete from Keyword k where k.group=:group")
    public void deleteByGroup(@Param("group")Groups group);

    @Modifying @Transactional
    @Query(value = "delete from Keyword k where k.group in :group")
    public void deleteByGroups(@Param("group")List<Groups> group);

    public List<Keyword> findByGroup(Groups group);

    @Modifying @Transactional
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE KEYWORD RESTART IDENTITY; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    public void truncateTable();
}
