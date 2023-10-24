package com.server.smallgroup.Repository;

import com.server.smallgroup.Entity.Hobby;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HobbyRepository extends JpaRepository<Hobby, Integer >  {

}
