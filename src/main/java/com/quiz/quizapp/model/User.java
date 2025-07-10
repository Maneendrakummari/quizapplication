package com.quiz.quizapp.model;

import jakarta.persistence.*;



@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;
    private String email;
    private String status = "active"; // default
    private String securityQuestion;  // ✅ Newly added
    private String securityAnswer;    // ✅ Used for offline password reset

    // Getters and Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getSecurityQuestion() { return securityQuestion; }
    public void setSecurityQuestion(String securityQuestion) { this.securityQuestion = securityQuestion; }

    public String getSecurityAnswer() { return securityAnswer; }
    public void setSecurityAnswer(String securityAnswer) { this.securityAnswer = securityAnswer; }
}
