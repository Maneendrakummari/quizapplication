package com.quiz.quizapp.config;

import com.quiz.quizapp.security.AdminDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

@Configuration
public class SecurityConfig {

    @Autowired
    private AdminDetailsService adminDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/questions/**",
                    "/api/auth/**",
                    "/uploads/**",
                    "/api/admin/login" 
                ).permitAll()
                .requestMatchers("/api/admin/**").permitAll()
 // ✅ FIXED PATH
                .anyRequest().authenticated()
            )
            .httpBasic(httpBasic -> httpBasic.disable())
            .formLogin(form -> form.disable())
            .exceptionHandling(e -> 
                e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
