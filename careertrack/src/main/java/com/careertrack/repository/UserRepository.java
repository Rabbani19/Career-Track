package com.careertrack.repository;

import com.careertrack.enums.Role;
import com.careertrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // Used by Admin dashboard
    long countByActive(Boolean active);

    long countByRole(Role role);
}