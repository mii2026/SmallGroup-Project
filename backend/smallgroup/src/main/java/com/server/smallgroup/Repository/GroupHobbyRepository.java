package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.GroupHobby;
import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.UserHobby;
import com.server.smallgroup.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GroupHobbyRepository extends JpaRepository<GroupHobby, Long> {
    @Modifying @Transactional
    @Query(value = "delete from GroupHobby gh where gh.group=:group")
    public void deleteByGroup(@Param("group") Groups g);

    @Modifying @Transactional
    @Query(value = "delete from GroupHobby gh where gh.group in :group")
    public void deleteByGroups(@Param("group") List<Groups> g);


    @Query(value = "select gh from GroupHobby gh join fetch gh.hobby where gh.group=:group")
    public List<GroupHobby> findByGroup(@Param("group") Groups g);

    @Modifying @Transactional
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE GROUP_HOBBY RESTART IDENTITY; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    public void truncateTable();
}
