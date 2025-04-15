package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import antlr.HTMLCodeGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @PostMapping("/register")
    public ResponseEntity<Account> registerUser(@RequestBody Account account){
        if(!accountService.isAccountValid(account)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        if(accountService.isUsernameTaken(account.getUsername())){
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Account savedAccount = accountService.saveAccount(account);
        return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<Account> loginUser(@RequestBody Account account){
        Account loggedInAccount = accountService.loginUser(account);
        return loggedInAccount != null ? ResponseEntity.ok(loggedInAccount) : ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        Message createdMessage = messageService.creatMessage(message);
        return createdMessage != null ? ResponseEntity.ok(createdMessage) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer messageId){
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Integer> deleteMessage(@PathVariable Integer messageId){
        int result = messageService.deleteMessage(messageId);
        return result == 1 ? ResponseEntity.ok(result) : ResponseEntity.ok().build();
    }

    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer messageId, @RequestBody Map<String,String> request) {
        String newText = request.get("messageText");
        if (newText == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        int result = messageService.updateMessage(messageId, newText);
        return result == 1 ? ResponseEntity.ok(result) : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByUser(@PathVariable Integer accountId){
        return ResponseEntity.ok(messageService.getMessagesByUser(accountId));
    }
}
