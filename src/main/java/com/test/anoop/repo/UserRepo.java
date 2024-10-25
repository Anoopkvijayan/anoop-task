package com.test.anoop.repo;

import com.test.anoop.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<UserInfo, Integer> {

    Optional<UserInfo> findByEmail(String email);

    Optional<UserInfo> findByName(String email);


}
