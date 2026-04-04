package com.p3springboot.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.p3springboot.model.UserEntity;
import com.p3springboot.services.MessageService;
import com.p3springboot.services.UserService;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    //DTO
    public static record CreateMessageRequest(
        String message,
        int rental_id,
        int user_id
    ) {}

    public static record MessageResponse(String message){}

    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> createMessage(@RequestBody CreateMessageRequest request, 
                                                        Authentication authentication) {

        String email = authentication.getName();
        UserEntity user = userService.findByEmail(email);
        
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        if (user.getId() != (long) request.user_id()) {
            return ResponseEntity.status(403).build();
        }

        messageService.createMessage(request.message(), request.rental_id(), request.user_id());
        return ResponseEntity.ok(new MessageResponse("Message sent successfully"));
    }
}
