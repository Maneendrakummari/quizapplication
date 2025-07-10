package com.quiz.quizapp.repository;

import com.quiz.quizapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    // Check if username is already taken
    boolean existsByUsername(String username);

    // For login: match username and password
    User findByUsernameAndPassword(String username, String password);

    // ✅ For offline password reset (username lookup)
    User findByUsername(String username);
}
