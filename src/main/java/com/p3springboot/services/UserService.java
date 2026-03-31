package com.p3springboot.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.p3springboot.model.UserEntity;
import com.p3springboot.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity registerUser(String email, String name,String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            return null;
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        
        return userRepository.save(user);
    }  

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public Optional<UserEntity> findById(int id) {
        return userRepository.findById((long)id);
    }

    public Object getUserById(int userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }
}
