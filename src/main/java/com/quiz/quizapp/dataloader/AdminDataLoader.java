package com.quiz.quizapp.dataloader;

import com.quiz.quizapp.model.Admin;
import com.quiz.quizapp.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminDataLoader implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (adminRepository.findByUsername("admin").isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            adminRepository.save(admin);
            System.out.println("✅ Admin user created!");
        }
    }
}
