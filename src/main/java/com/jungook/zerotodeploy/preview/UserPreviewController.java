package com.jungook.zerotodeploy.preview;

import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/user")
public class UserPreviewController {
    private final JoinUserRepo joinUserRepo;

    public UserPreviewController(JoinUserRepo joinUserRepo) {
        this.joinUserRepo = joinUserRepo;
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
}
