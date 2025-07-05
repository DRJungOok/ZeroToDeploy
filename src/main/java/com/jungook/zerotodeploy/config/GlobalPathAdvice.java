package com.jungook.zerotodeploy.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.jungook.zerotodeploy.joinMember.JoinUserRepo;

@ControllerAdvice

@Configuration
public class GlobalPathAdvice {
    private final JoinUserRepo joinUserRepo;

    public GlobalPathAdvice(JoinUserRepo joinUserRepo) {
        this.joinUserRepo = joinUserRepo;
    }

    @ModelAttribute("currentPath")
    public String setCurrentPath(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("currentUserName")

    public String currentUserName(Authentication authentication) {
        if (authentication == null) return null;

        Object principal = authentication.getPrincipal();
        final String loginId = (principal instanceof UserDetails userDetails)
                ? userDetails.getUsername()
                : authentication.getName();

        return joinUserRepo.findByUserName(loginId)
                .or(() -> joinUserRepo.findByEmail(loginId))
                .map(user -> user.getUserName())
                .orElse(loginId);
    }
}
