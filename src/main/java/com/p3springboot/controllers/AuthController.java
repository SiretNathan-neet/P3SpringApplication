package com.p3springboot.controllers;

import java.time.format.DateTimeFormatter;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.p3springboot.model.UserEntity;
import com.p3springboot.services.JWTService;
import com.p3springboot.services.UserService;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    public final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;


    public AuthController(JWTService jwtService, AuthenticationManager authenticationManager, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    //DTO
    public record LoginRequest(String email, String password){}
    public record RegisterRequest(String email, String name, String password){}
    public record AuthenticationResponse(String token){}
    public record UserResponse(int id, 
                               String name, 
                               String email, 
                               String createdAt, 
                               String updatedAt){}

    @PostMapping("/auth/login")
    public ResponseEntity<AuthenticationResponse> getToken(@RequestBody LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
            )
        );
        String token = jwtService.generateToken(authentication);
        System.out.println("Generated Token : " + token); //Log temporaire pour vérifier la génération du token
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest) {

        UserEntity user = userService.registerUser(
            registerRequest.email(),
            registerRequest.name(),
            registerRequest.password()
        );
        
        if (user == null) {
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                registerRequest.email(),
                registerRequest.password()
            )
        );

        String token = jwtService.generateToken(authentication);
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/auth/me")
    public ResponseEntity<UserResponse> getMe(Authentication authentication) {
        
        String email = authentication.getName();
        UserEntity user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(401).build();
        }

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
