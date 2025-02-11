package com.jungook.zerotodeploy.user.register.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.jungook.zerotodeploy.user.register.repository.RegisterRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final RegisterRepo registerRepo;
    private final BCryptPasswordEncoder passwordEncoder;
}