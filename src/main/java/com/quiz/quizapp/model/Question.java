package com.quiz.quizapp.model;

import jakarta.persistence.*;

@Entity
@Table(name = "question", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"question"})
})
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String question;

    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctAnswer;
    private String category;

    private String imageUrl; // e.g., "/uploads/filename.jpg" or "none"

    // Constructors
    public Question() {
    }

    public Question(String question, String option1, String option2, String option3,
                    String option4, String correctAnswer, String category, String imageUrl) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
