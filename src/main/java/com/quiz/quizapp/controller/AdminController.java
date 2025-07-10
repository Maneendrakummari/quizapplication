package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.Question;
import com.quiz.quizapp.model.User;
import com.quiz.quizapp.model.QuizResult;
import com.quiz.quizapp.model.Admin;
import com.quiz.quizapp.dto.LoginRequest;
import com.quiz.quizapp.repository.AdminRepository;
import com.quiz.quizapp.Service.QuestionService;
import com.quiz.quizapp.Service.UserService;
import com.quiz.quizapp.Service.QuizResultService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserService userService;

    @Autowired
    private QuizResultService quizResultService;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Dashboard
    @GetMapping("/dashboard")
    public String dashboard() {
        return "✅ Welcome Admin!";
    }

    // ✅ Admin Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        return adminRepository.findByUsername(request.getUsername())
            .map(admin -> {
                boolean matches = passwordEncoder.matches(request.getPassword(), admin.getPassword());
                if (matches) {
                    return ResponseEntity.ok("✅ Welcome Admin!");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .body("❌ Invalid credentials");
                }
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("❌ Invalid credentials"));
    }


    


    // ----------------------------
    // QUESTIONS
    // ----------------------------

    @PostMapping("/questions")
    public Question addQuestion(@RequestBody Question question) {
        return questionService.addQuestion(question)
                .orElseThrow(() -> new RuntimeException("Question already exists!"));
    }

    @PutMapping("/questions/{id}")
    public Question updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        return questionService.updateQuestion(id, question)
                .orElseThrow(() -> new RuntimeException("Question not found with ID " + id));
    }

    @DeleteMapping("/questions/{id}")
    public void deleteQuestion(@PathVariable Long id) {
        questionService.deleteQuestion(id);
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    // ----------------------------
    // USERS
    // ----------------------------

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user)
                .orElseThrow(() -> new RuntimeException("Username already exists!"));
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + id));
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID " + id));
    }

    // ----------------------------
    // QUIZ RESULTS
    // ----------------------------

    @GetMapping("/results")
    public List<QuizResult> getAllResults() {
        return quizResultService.getAllResults();
    }

    @GetMapping("/results/user/{username}")
    public List<QuizResult> getResultsByUsername(@PathVariable String username) {
        return quizResultService.getResultsByUsername(username);
    }

    @DeleteMapping("/results/{id}")
    public void deleteResult(@PathVariable Long id) {
        quizResultService.deleteResult(id);
    }
}
