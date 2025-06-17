package com.jungook.zerotodeploy.preview;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/api/user")
public class UserPreviewController {
    private final JoinUserRepo joinUserRepo;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserPreviewController(JoinUserRepo joinUserRepo, BCryptPasswordEncoder passwordEncoder) {
        this.joinUserRepo = joinUserRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/preview/{username}")
    public ResponseEntity<?> previewUser(@PathVariable("username") String username) {
        return joinUserRepo.findByUserName(username)
                .map(user -> {
                    Map<String, Object> preview = new HashMap<>();
                    preview.put("name", user.getUserName());
                    preview.put("email", user.getEmail());
                    preview.put("createdAt", user.getJoinDate());
                    return ResponseEntity.ok(preview);
                }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/myInfo/{username}")
    public String userInfo(@PathVariable String username,
                           Model model,
                           Authentication authentication) {

        String currentUsername = authentication.getName();

        log.info("요청한 username: {}", username);
        log.info("로그인한 사용자: {}", currentUsername);

        JoinUserEntity user = joinUserRepo.findByUserName(username)
                .orElseThrow(() -> {
                    log.warn("User not found for username: {}", username);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
                });

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(user.getUserName());

        model.addAttribute("user", user);
        model.addAttribute("isEditable", isAdmin || isOwner);

        return "myInfo";
    }

    @PostMapping("/myInfo/{username}/update")
    @Transactional
    public String updateInfo(@PathVariable String username,
                             @RequestParam("userName") String newUserName,
                             @RequestParam("email") String email,
                             @RequestParam(required = false) String password,
                             Authentication authentication) {

        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(username);

        if (!isAdmin && !isOwner) {
            return "redirect:/access-denied";
        }

        JoinUserEntity user = joinUserRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (!user.getUserName().equals(newUserName)
                && joinUserRepo.existsByUserName(newUserName)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 사용자명입니다.");
        }

        user.setUserName(newUserName);
        user.setEmail(email);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        joinUserRepo.save(user);

        // ✅ CustomUserDetails로 Security 세션 갱신
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                user.getPassword(),
                authentication.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/api/user/myInfo/" + user.getUserName() + "?success";
    }
}
