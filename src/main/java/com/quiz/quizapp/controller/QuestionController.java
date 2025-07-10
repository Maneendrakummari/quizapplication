package com.quiz.quizapp.controller;

import com.quiz.quizapp.dto.QuizResultDTO;
import com.quiz.quizapp.dto.QuizSubmissionDTO;
import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.model.QuizResult;
import com.quiz.quizapp.repository.QuestionRepository;
import com.quiz.quizapp.repository.QuizResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizResultRepository quizResultRepository;

    // ✅ Upload image
    @PostMapping("/upload")
    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        File destination = new File(uploadDir + filename);
        file.transferTo(destination);
        return "/uploads/" + filename;
    }

    // ✅ Add single question
    @PostMapping
    public Question addQuestion(@RequestBody Question question) {
        if (question.getImageUrl() == null || question.getImageUrl().isBlank()) {
            question.setImageUrl("none");
        }

        questionRepository.findByQuestionIgnoreCase(question.getQuestion()).ifPresent(existing -> {
            throw new RuntimeException("Duplicate question not allowed.");
        });

        return questionRepository.save(question);
    }

    // ✅ Get all questions
    @GetMapping
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // ✅ Get questions by category
    @GetMapping("/category/{category}")
    public List<Question> getByCategory(@PathVariable String category) {
        return questionRepository.findByCategoryIgnoreCase(category);
    }

    // ✅ Get limited questions
    @GetMapping("/category/{category}/limit/{limit}")
    public List<Question> getLimitedQuestionsByCategory(@PathVariable String category, @PathVariable int limit) {
        if (limit > 20) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit cannot be greater than 20.");
        }

        List<Question> all = questionRepository.findByCategoryIgnoreCase(category);
        Collections.shuffle(all);
        return all.stream().limit(limit).toList();
    }

    // ✅ Add bulk questions
    @PostMapping("/bulk")
    public Map<String, Object> addBulkQuestions(@RequestBody List<Question> questions) {
        List<Question> saved = new ArrayList<>();
        List<String> skipped = new ArrayList<>();

        for (Question q : questions) {
            if (q.getImageUrl() == null || q.getImageUrl().isBlank()) {
                q.setImageUrl("none");
            }
            if (questionRepository.findByQuestionIgnoreCase(q.getQuestion()).isEmpty()) {
                saved.add(q);
            } else {
                skipped.add(q.getQuestion());
            }
        }

        questionRepository.saveAll(saved);

        Map<String, Object> result = new HashMap<>();
        result.put("savedCount", saved.size());
        result.put("skippedDuplicates", skipped);
        return result;
    }

    // ✅ Get questions by IDs
    @PostMapping("/byIds")
    public List<Question> getQuestionsByIds(@RequestBody List<Long> ids) {
        return questionRepository.findAllById(ids);
    }

    // ✅ Submit quiz and store result
    @PostMapping("/submit")
    public QuizResultDTO submitQuiz(@RequestBody QuizSubmissionDTO submissionDTO) {
        System.out.println("📥 Submission from: " + submissionDTO.getUsername());

        if (submissionDTO.getAnswers() == null || submissionDTO.getAnswers().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No answers submitted.");
        }

        int total = submissionDTO.getAnswers().size();
        int correctCount = 0;
        List<QuizResultDTO.QuestionResult> results = new ArrayList<>();

        for (QuizSubmissionDTO.AnswerDTO answer : submissionDTO.getAnswers()) {
            Long questionId = answer.getQuestionId();
            String selectedAnswer = answer.getSelectedAnswer();
            System.out.println("➡ QID: " + questionId + " | Selected: " + selectedAnswer);

            Question question = questionRepository.findById(questionId).orElse(null);
            boolean correct = false;
            String correctAnswer = "";

            if (question == null) {
                System.out.println("❌ Question not found: " + questionId);
                continue; // Skip invalid question
            }

            correctAnswer = question.getCorrectAnswer();
            if (correctAnswer == null || correctAnswer.isBlank()) {
                System.out.println("⚠️ No correct answer set for QID: " + questionId);
                continue; // Skip if correct answer is not set
            }

            if (correctAnswer.equalsIgnoreCase(selectedAnswer)) {
                correctCount++;
                correct = true;
            }

            QuizResultDTO.QuestionResult qr = new QuizResultDTO.QuestionResult();
            qr.setQuestionId(questionId);
            qr.setCorrect(correct);
            qr.setCorrectAnswer(correctAnswer);
            qr.setSelectedAnswer(selectedAnswer);
            results.add(qr);
        }

        double percentage = total > 0 ? ((double) correctCount / total) * 100 : 0.0;

        QuizResultDTO quizResult = new QuizResultDTO();
        quizResult.setUsername(submissionDTO.getUsername());
        quizResult.setTotalQuestions(total);
        quizResult.setCorrectAnswers(correctCount);
        quizResult.setPercentage(percentage);
        quizResult.setResults(results);

        QuizResult entity = new QuizResult();
        entity.setUsername(submissionDTO.getUsername());
        entity.setTotalQuestions(total);
        entity.setCorrectAnswers(correctCount);
        entity.setScorePercentage(percentage);
        entity.setSubmittedAt(LocalDateTime.now());

        quizResultRepository.save(entity);

        System.out.println("✅ Submission saved. Score: " + percentage + "%");

        return quizResult;
    }

    // ✅ Get all results
    @GetMapping("/results")
    public List<QuizResult> getAllResults() {
        return quizResultRepository.findAll();
    }

    // ✅ Get results by username
    @GetMapping("/results/{username}")
    public List<QuizResult> getResultsByUser(@PathVariable String username) {
        return quizResultRepository.findByUsername(username);
    }

    // ✅ Delete one question
    @DeleteMapping("/{id}")
    public Map<String, String> deleteQuestionById(@PathVariable Long id) {
        if (questionRepository.existsById(id)) {
            questionRepository.deleteById(id);
            return Map.of("message", "Question deleted", "id", id.toString());
        } else {
            throw new RuntimeException("Question not found with ID: " + id);
        }
    }

    // ✅ Delete multiple
    @DeleteMapping("/bulk")
    public Map<String, Object> deleteMultipleQuestions(@RequestBody List<Long> ids) {
        List<Long> deleted = new ArrayList<>();
        List<Long> notFound = new ArrayList<>();

        for (Long id : ids) {
            if (questionRepository.existsById(id)) {
                questionRepository.deleteById(id);
                deleted.add(id);
            } else {
                notFound.add(id);
            }
        }

        return Map.of(
                "deletedCount", deleted.size(),
                "deletedIds", deleted,
                "notFoundIds", notFound
        );
    }
}
