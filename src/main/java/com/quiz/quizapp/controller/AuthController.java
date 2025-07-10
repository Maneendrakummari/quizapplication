package com.quiz.quizapp.controller;

import com.quiz.quizapp.model.User;
import java.util.Optional;
import com.quiz.quizapp.Service.UserService;

import com.quiz.quizapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ User Registration
   
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Password is required");
        }
        if (userService.getUserByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("active");

        User saved = userService.addUser(user).orElse(null);
        if (saved == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not save user");
        }

        User safeUser = new User();
        safeUser.setId(saved.getId());
        safeUser.setUsername(saved.getUsername());
        safeUser.setEmail(saved.getEmail());
        safeUser.setStatus(saved.getStatus());
        return ResponseEntity.status(HttpStatus.CREATED).body(safeUser);
    }

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        // Validate input
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username must not be empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password must not be empty");
        }

        User found = userRepo.findByUsername(user.getUsername());
        if (found == null || found.getPassword() == null || found.getPassword().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        // Optional: verify stored password format looks like bcrypt (optional)
        if (!(found.getPassword().startsWith("$2a$") || found.getPassword().startsWith("$2b$") || found.getPassword().startsWith("$2y$"))) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Stored password format invalid");
        }

        // Match raw password with bcrypt hashed password
        if (!passwordEncoder.matches(user.getPassword(), found.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        if ("blocked".equalsIgnoreCase(found.getStatus())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is blocked");
        }

        // Return user info without sensitive data (optional but recommended)
        User safeUser = new User();
        safeUser.setId(found.getId());
        safeUser.setUsername(found.getUsername());
        safeUser.setEmail(found.getEmail());
        safeUser.setStatus(found.getStatus());
        // add other non-sensitive fields as needed

        return ResponseEntity.ok(safeUser);
    }



    // ✅ Offline Reset Password via Username + Security Answer
    @PostMapping("/reset-password-offline")
    public ResponseEntity<?> resetPasswordOffline(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String answer = request.get("securityAnswer");
        String newPassword = request.get("newPassword");

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (user.getSecurityAnswer() == null || !user.getSecurityAnswer().equalsIgnoreCase(answer)) {
            return ResponseEntity.status(400).body("Security answer mismatch");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);

        return ResponseEntity.ok("Password reset successfully via security answer.");
    }

    // ✅ Online Password Change (Logged-in users)
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody Map<String, String> req) {
        String username = req.get("username");
        String oldPassword = req.get("oldPassword");
        String newPassword = req.get("newPassword");

        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return ResponseEntity.status(400).body("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
        return ResponseEntity.ok("Password changed successfully.");
    }

    // ✅ Admin: List All Users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    // ✅ Admin: Block a user
    @PutMapping("/block/{username}")
    public ResponseEntity<?> blockUser(@PathVariable String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setStatus("blocked");
        userRepo.save(user);
        return ResponseEntity.ok("User blocked successfully.");
    }

    // ✅ Admin: Unblock a user
    @PutMapping("/unblock/{username}")
    public ResponseEntity<?> unblockUser(@PathVariable String username) {
        User user = userRepo.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        user.setStatus("active");
        userRepo.save(user);
        return ResponseEntity.ok("User unblocked successfully.");
    }
}
