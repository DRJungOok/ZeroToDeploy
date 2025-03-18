package com.jungook.zerotodeploy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jungook.zerotodeploy.model.RegisterEntity;
import com.jungook.zerotodeploy.service.RegisterService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<String> registerUser(@RequestBody RegisterEntity registerEntity) {
        try {
            registerService.registerUser(registerEntity);
            return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}