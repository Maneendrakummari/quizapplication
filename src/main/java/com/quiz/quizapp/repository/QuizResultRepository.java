package com.quiz.quizapp.repository;

import com.quiz.quizapp.model.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
    List<QuizResult> findByUsername(String username);
}
