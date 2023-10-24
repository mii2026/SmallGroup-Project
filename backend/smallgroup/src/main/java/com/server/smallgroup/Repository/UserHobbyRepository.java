package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.UserHobby;
import com.server.smallgroup.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserHobbyRepository extends JpaRepository<UserHobby, Long> {

    @Query(value = "select uh from UserHobby uh join fetch uh.hobby where uh.user=:user")
    public List<UserHobby> findByUser(@Param("user")Users user);

    @Modifying @Transactional
    @Query(value = "delete from UserHobby uh where uh.user=:user")
    public void deleteByUser(@Param("user")Users user);

    @Modifying @Transactional
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE USER_HOBBY RESTART IDENTITY; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    public void truncateTable();
}
