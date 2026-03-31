package com.p3springboot.controllers;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.p3springboot.model.UserEntity;
import com.p3springboot.services.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //DTO
    public static record UserResponse(int id, 
                                      String name, 
                                      String email, 
                                      String createdAt, 
                                      String updatedAt) {}
    
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        Optional <UserEntity> userOpt = userService.findById(id);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        UserEntity user = userOpt.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        return ResponseEntity.ok(new UserResponse(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getCreatedAt().format(formatter),
            user.getUpdatedAt().format(formatter)
        ));
    }

}
