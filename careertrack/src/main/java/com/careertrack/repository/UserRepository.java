package com.careertrack.repository;

import com.careertrack.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
        extends JpaRepository<User, Long> {

    // ✅ Find by username
    Optional<User> findByUsername(String username);

    // ✅ Find by email
    Optional<User> findByEmail(String email);

    // ✅ Check exists
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}