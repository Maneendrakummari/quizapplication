package com.quiz.quizapp.util;

import com.quiz.quizapp.model.Admin;
import com.quiz.quizapp.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HashAdminPassword implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // use bean-injected encoder

    
    @Override
    public void run(String... args) {
        System.out.println("🔁 HashAdminPassword runner executing...");

        List<Admin> admins = adminRepository.findAll();
        System.out.println("🧾 Found admins: " + admins.size());

        for (Admin admin : admins) {
            String current = admin.getPassword();
            System.out.println("👀 Checking password for: " + admin.getUsername() + " | Raw: " + current);

            if (!(current.startsWith("$2a$") || current.startsWith("$2b$") || current.startsWith("$2y$"))) {
                String hashed = passwordEncoder.encode(current);
                admin.setPassword(hashed);
                adminRepository.save(admin);
                System.out.println("✅ Hashed password for admin: " + admin.getUsername());
            }
        }
    }

    }

