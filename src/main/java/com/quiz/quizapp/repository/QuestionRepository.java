package com.quiz.quizapp.repository;

import com.quiz.quizapp.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Find questions by category (case-insensitive)
    List<Question> findByCategoryIgnoreCase(String category);

    // Check for duplicates based on question text
    Optional<Question> findByQuestionIgnoreCase(String question);
}
