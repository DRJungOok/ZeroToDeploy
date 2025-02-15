package com.jungook.zerotodeploy.user.register.controller;

import org.springframework.stereotype.Controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jungook.zerotodeploy.user.register.repository.RegisterEntity;
import com.jungook.zerotodeploy.user.register.service.RegisterService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor

@Controller
public class RegisterController {
    private final RegisterService registerService;
    
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody RegisterEntity entity) {
        try {
            registerService.registerUser(entity);
            return ResponseEntity.ok("User registered successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
}
