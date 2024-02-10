package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.entity.Message;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

import java.util.List;
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    private final AccountRepository accountRepository;

    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message postMessage(Message message) {
        validateMessageAuthorId(message);
        validateMessageLength(message);
        return messageRepository.save(message);
    }

    public Message getMessage(Integer message_id) {
        return messageRepository.findById(message_id).orElse(null);
    }

    public int editMessage(Integer message_id, Message message) {
        Message messageFromDb = validateMessageId(message_id);
        
        validateMessageLength(message);

        messageFromDb.setMessage_text(message.getMessage_text());
        if (messageRepository.save(messageFromDb) == null) {
            return 0;
        }
        return 1;
    }

    public int deleteMessage(Integer message_id) {
        boolean exists = messageRepository.existsById(message_id);
        if (exists) {
            messageRepository.deleteById(message_id);
            return 1; // Since deleteById is void, we assume 1 row is updated when the message exists
        }
        return 0; // No rows updated if the message does not exist
    }

    public List<Message> findMessagesByAccountId(Integer account_id) {
        return messageRepository.findMessagesByAccountId(account_id);
    }

    private void validateMessageAuthorId(Message message) {
        if (message.getPosted_by() == null || !accountRepository.findById(message.getPosted_by()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not exist");
        }
    }

    private Message validateMessageId(Integer message_id) {
        Message messageFromDb = messageRepository.findById(message_id).orElse(null);
        if (messageFromDb == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Message does not exist");
        }
        return messageFromDb;
    }

    private void validateMessageLength(Message message) {
        if (message.getMessage_text().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content cannot be blank");
        }
        if (message.getMessage_text().length() > 255) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content cannot be more than 255 characters long");
        }
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }
}
