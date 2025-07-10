package com.quiz.quizapp.Service;

import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    // Add a new question if not duplicate
    public Optional<Question> addQuestion(Question question) {
        Optional<Question> existing = questionRepository.findByQuestionIgnoreCase(question.getQuestion());
        if (existing.isPresent()) {
            return Optional.empty();
        }
        return Optional.of(questionRepository.save(question));
    }

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> getByCategory(String category) {
        return questionRepository.findByCategoryIgnoreCase(category);
    }

    public Optional<Question> updateQuestion(Long id, Question newData) {
        return questionRepository.findById(id).map(existing -> {
            existing.setQuestion(newData.getQuestion());
            existing.setOption1(newData.getOption1());
            existing.setOption2(newData.getOption2());
            existing.setOption3(newData.getOption3());
            existing.setOption4(newData.getOption4());
            existing.setCorrectAnswer(newData.getCorrectAnswer());
            existing.setCategory(newData.getCategory());
            existing.setImageUrl(newData.getImageUrl());
            return questionRepository.save(existing);
        });
    }

    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
    }
}
