package com.quiz.quizapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
public class QuizResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private int totalQuestions;
    private int correctAnswers;
    private double scorePercentage;
    private LocalDateTime submittedAt;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public double getScorePercentage() { return scorePercentage; }
    public void setScorePercentage(double scorePercentage) { this.scorePercentage = scorePercentage; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }
}
