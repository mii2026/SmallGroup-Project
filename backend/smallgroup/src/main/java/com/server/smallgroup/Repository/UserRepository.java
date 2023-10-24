package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    public Optional<Users> findByUserId(String userId);
    public Optional<Users> findByPhoneNum(String phoneNum);
    public Optional<Users> findByUserIdAndPw(String userId, String pw);
    public Optional<Users> findByNameAndPhoneNum(String name, String phoneNum);
    public Optional<Users> findByNameAndUserIdAndPhoneNum(String name, String userId, String PhoneNum);
    @Modifying @Transactional
    @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE USERS RESTART IDENTITY; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    public void truncateTable();
}
