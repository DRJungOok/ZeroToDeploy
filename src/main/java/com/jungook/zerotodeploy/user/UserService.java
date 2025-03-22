package com.jungook.zerotodeploy.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserEntity create(String username, String password, String email) {
        UserEntity siteUser = new UserEntity();
        siteUser.setUsername(username);
        siteUser.setEmail(email);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        siteUser.setPassword(passwordEncoder.encode(password));
        this.userRepository.save(siteUser);
        return siteUser;
    }
}
