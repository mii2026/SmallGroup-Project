package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.Groups;
import com.server.smallgroup.Entity.UserGroup;
import com.server.smallgroup.Entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
    @Modifying @Transactional
    @Query(value = "delete from UserGroup ug where ug.user=:user")
    public void deleteByUser(@Param("user") Users user);

    @Modifying @Transactional
    @Query(value = "delete from UserGroup ug where ug.group=:group")
    public void deleteByGroup(@Param("group") Groups group);

    @Modifying @Transactional
    @Query(value = "delete from UserGroup ug where ug.group in :groups")
    public void deleteByGroups(@Param("groups") List<Groups> grouplist);

    @Modifying @Transactional
    @Query(value = "delete from UserGroup ug where ug.group=:group and ug.user=:user")
    public void deleteByGroupAndUser(@Param("group") Groups group, @Param("user") Users user);

    @Query(value = "select ug from UserGroup ug join fetch ug.group where ug.user=:user",
            countQuery = "select count(ug) from UserGroup ug where ug.user=:user")
    public Page<UserGroup> findByUser(@Param("user") Users u, Pageable pageable);

    @Query(value = "select ug from UserGroup ug join fetch ug.user where ug.group=:group")
    public List<UserGroup> findByGroup(@Param("group") Groups g);

    public Optional<UserGroup> findByGroupAndUser(Groups g, Users u);

    public List<UserGroup> findByUserAndIsCaptain(Users u, Boolean b);

    @Modifying @Transactional
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE USER_GROUP RESTART IDENTITY; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    public void truncateTable();
}
