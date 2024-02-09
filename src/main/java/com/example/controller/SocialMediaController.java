package com.example.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.entity.Account;
import com.example.service.AccountService;
import com.example.repository.AccountRepository;

import com.example.entity.Message;
import com.example.service.MessageService;
import com.example.repository.MessageRepository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(AccountRepository accountRepository, MessageRepository messageRepository) {
        accountService = new AccountService(accountRepository);
        messageService = new MessageService(messageRepository, accountRepository);
    }
    
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        try {
            Account registeredAccount = accountService.register(account);
            return ResponseEntity.ok(registeredAccount);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.login(account));
    }
    
    @PostMapping("/messages")
    public ResponseEntity<Message> postMessage(@RequestBody Message message) {
        try {
            Message postedMessage = messageService.postMessage(message);
            return ResponseEntity.ok(postedMessage);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(null);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessage(@PathVariable("message_id") int message_id) {
        return ResponseEntity.ok(messageService.getMessage(message_id));
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Object> deleteMessage(@PathVariable("message_id") Integer message_id) {
        int rowsUpdated = messageService.deleteMessage(message_id);
        if (rowsUpdated == 0) {
            return ResponseEntity.ok().body(""); // Empty body for idempotency if the message does not exist
        }
        return ResponseEntity.ok().body(rowsUpdated); // Return the number of rows updated (1) if the message existed
    }

}