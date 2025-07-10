package com.quiz.quizapp.dto;

import java.util.List;

public class QuizSubmissionDTO {
    private String username;  // ✅ Required for storing quiz results
    private List<AnswerDTO> answers;

    // ✅ Getter & Setter for username
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<AnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerDTO> answers) {
        this.answers = answers;
    }

    // ✅ Nested class for each answer
    public static class AnswerDTO {
        private Long questionId;
        private String selectedAnswer;

        public Long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Long questionId) {
            this.questionId = questionId;
        }

        public String getSelectedAnswer() {
            return selectedAnswer;
        }

        public void setSelectedAnswer(String selectedAnswer) {
            this.selectedAnswer = selectedAnswer;
        }
    }
}
