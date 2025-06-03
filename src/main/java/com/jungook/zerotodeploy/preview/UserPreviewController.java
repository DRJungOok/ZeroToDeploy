package com.jungook.zerotodeploy.preview;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

        JoinUserEntity user = joinUserRepo.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(user.getUserName());

        model.addAttribute("user", user);
        model.addAttribute("isEditable", isAdmin || isOwner);

        return "myInfo";
    }

    @PostMapping("/myInfo/{username}/upload")
    @Transactional
    public String uploadProfile(@PathVariable String username,
                                @RequestParam MultipartFile profileImage,
                                Authentication authentication) throws IOException {
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(username);

        if (!isAdmin && !isOwner) return "redirect:/access-denied";

        JoinUserEntity user = joinUserRepo.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!profileImage.isEmpty()) {
            String filename = UUID.randomUUID() + "_" + profileImage.getOriginalFilename();
            Path path = Paths.get("/home/ubuntu/uploads/" + filename);
            Files.write(path, profileImage.getBytes());
            user.setProfileImage(filename);
        }

        return "redirect:/api/user/myInfo/" + username;
    }

    @PostMapping("/myInfo/{username}/update")
    @Transactional
    public String updateInfo(@RequestParam("userName") String username,
                             @RequestParam("email") String email,
                             @RequestParam(required = false) String password,
                             Authentication authentication) {
        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(username);

        if (!isAdmin && !isOwner) return "redirect:/access-denied";

        JoinUserEntity user = joinUserRepo.findByUserName(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(email);
        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        joinUserRepo.save(user);
        return "redirect:/api/user/myInfo/" + username + "?success";
    }
}