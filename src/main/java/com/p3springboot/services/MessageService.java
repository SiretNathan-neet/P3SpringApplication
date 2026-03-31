package com.p3springboot.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.p3springboot.model.MessageEntity;
import com.p3springboot.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public MessageEntity createMessage(String message, int rentalId, int userId ) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessage(message);
        messageEntity.setRentalId(rentalId);
        messageEntity.setUserId(userId);
        messageEntity.setCreatedAt(LocalDateTime.now());
        messageEntity.setUpdatedAt(LocalDateTime.now());
        
        return messageRepository.save(messageEntity);
    }
}
