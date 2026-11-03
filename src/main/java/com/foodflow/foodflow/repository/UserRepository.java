package com.foodflow.foodflow.repository;
import java.util.Optional;

import com.foodflow.foodflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

}
