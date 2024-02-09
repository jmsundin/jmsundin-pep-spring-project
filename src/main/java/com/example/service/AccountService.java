package com.example.service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import com.example.entity.Account;
import com.example.repository.AccountRepository;


@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account register(Account account) {
        validate(account);
        try {
            return accountRepository.save(account);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration failed");
        }
    }

    private void validate(Account account) {
        if (account.getUsername() == null || account.getUsername().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank");
        }
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 4 characters long");
        }
    }
}
