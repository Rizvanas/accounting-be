package com.rizvanchalilovas.accountingbe.repositories;

import com.rizvanchalilovas.accountingbe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    List<User> findByUsernameContains(String username);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
