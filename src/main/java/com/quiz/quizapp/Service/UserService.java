package com.quiz.quizapp.Service;

import com.quiz.quizapp.model.User;
import com.quiz.quizapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> addUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return Optional.empty(); // Duplicate username
        }
        return Optional.of(userRepository.save(user));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        return userRepository.findById(id).map(existing -> {
            existing.setUsername(updatedUser.getUsername());
            existing.setPassword(updatedUser.getPassword());
            return userRepository.save(existing);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
