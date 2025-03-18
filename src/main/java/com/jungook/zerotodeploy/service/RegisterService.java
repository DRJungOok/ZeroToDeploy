package com.jungook.zerotodeploy.service;

import java.util.Optional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.jungook.zerotodeploy.model.RegisterEntity;
import com.jungook.zerotodeploy.repository.RegisterRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final RegisterRepo registerRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegisterEntity registerUser(RegisterEntity registerEntity) {
        Optional<RegisterEntity> existingUser = registerRepo.findByUsername(registerEntity.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Optional<RegisterEntity> existingEmail = registerRepo.findByEmail(registerEntity.getEmail());
        if (existingEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        registerEntity.encryptPassword(passwordEncoder);
        return registerRepo.save(registerEntity);
    }
}
