package com.quiz.quizapp.Service;

import com.quiz.quizapp.model.QuizResult;
import com.quiz.quizapp.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuizResultService {

    private final QuizResultRepository quizResultRepository;

    @Autowired
    public QuizResultService(QuizResultRepository quizResultRepository) {
        this.quizResultRepository = quizResultRepository;
    }

    /**
     * Get all quiz results.
     *
     * @return list of all quiz results
     */
    public List<QuizResult> getAllResults() {
        return quizResultRepository.findAll();
    }

    /**
     * Get a quiz result by its ID.
     *
     * @param id the quiz result ID
     * @return optional containing the quiz result if found
     */
    public Optional<QuizResult> getResultById(Long id) {
        return quizResultRepository.findById(id);
    }

    /**
     * Get all quiz results for a specific user.
     *
     * @param username the username
     * @return list of quiz results for that user
     */
    public List<QuizResult> getResultsByUsername(String username) {
        return quizResultRepository.findByUsername(username);
    }

    /**
     * Save or update a quiz result.
     *
     * @param result the quiz result to save
     * @return the saved quiz result
     */
    public QuizResult saveResult(QuizResult result) {
        return quizResultRepository.save(result);
    }

    /**
     * Delete a quiz result by ID.
     *
     * @param id the quiz result ID
     */
    public void deleteResult(Long id) {
        quizResultRepository.deleteById(id);
    }
}
