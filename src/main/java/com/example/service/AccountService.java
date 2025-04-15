package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public boolean isAccountValid(Account account){
        return account.getUsername() != null && !account.getUsername().trim().isEmpty() && account.getPassword() != null && account.getPassword().length() >=4;
    }

    public boolean isUsernameTaken(String username){
        return accountRepository.findByUsername(username) != null;

    }

    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }

    public Account loginUser(Account account) {
        Account existing = accountRepository.findByUsername(account.getUsername());

        if(existing != null && existing.getPassword().equals(account.getPassword())){
            return existing;
        }
        return null;
    }





}
