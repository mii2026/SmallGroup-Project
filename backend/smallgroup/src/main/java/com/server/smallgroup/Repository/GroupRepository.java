package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.Groups;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRepository extends JpaRepository<Groups, Long> {

    @Query(value = "select g from Groups g join fetch g.keyword " +
            "where g.minAge>=:minAge and g.maxAge<=:maxAge and g.address like :address order by g.numPerson DESC",
            countQuery = "select count(g) from Groups g " +
                    "where g.minAge>=:minAge and g.maxAge<=:maxAge and g.address like :address")
    public Page<Groups> findByKeywordOrderByNumPerson(@Param("minAge")Integer minAge, @Param("maxAge")Integer maxAge,
                                                        @Param("address")String address, Pageable pageable);

    @Query(value = "select g from Groups g join fetch g.keyword " +
            "where g.minAge>=:minAge and g.maxAge<=:maxAge and g.address like :address order by g.createTime DESC",
            countQuery = "select count(g) from Groups g " +
                    "where g.minAge>=:minAge and g.maxAge<=:maxAge and g.address like :address")
    public Page<Groups> findByKeywordOrderByCreateTime(@Param("minAge")Integer minAge, @Param("maxAge")Integer maxAge,
                                                        @Param("address")String address, Pageable pageable);

    @Query(value = "select g from Groups g order by rand() limit :num")
    public List<Groups> randomGroups(@Param("num") int num);

    @Modifying @Transactional
    @Query(value = "delete from Groups g where g.id in :ids")
    public void deleteByIds(@Param("ids") List<Long> ids);

    @Modifying @Transactional @Query(value = "SET REFERENTIAL_INTEGRITY FALSE; TRUNCATE TABLE GROUPs RESTART IDENTITY; SET REFERENTIAL_INTEGRITY TRUE;", nativeQuery = true)
    void truncateTable();
}
