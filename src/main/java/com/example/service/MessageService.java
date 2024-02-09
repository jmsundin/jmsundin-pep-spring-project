package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message postMessage(Message message) {
        validateMessage(message);
        return messageRepository.save(message);
    }

    private void validateMessage(Message message) {
        if (message.getPosted_by() == null || !accountRepository.findById(message.getPosted_by()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
        if (message.getMessage_text().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content cannot be blank");
        }
        if (message.getMessage_text().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content cannot be more than 255 characters long");
        }
    }
}
