package com.plantstoen.api.repo;

import com.plantstoen.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUid(String email);
}
