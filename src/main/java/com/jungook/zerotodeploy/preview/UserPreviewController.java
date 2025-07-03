package com.jungook.zerotodeploy.preview;

import com.jungook.zerotodeploy.details.CustomUserDetails;
import com.jungook.zerotodeploy.friends.FriendsEntity;
import com.jungook.zerotodeploy.friends.FriendsRepo;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import jakarta.servlet.http.HttpServletRequest;
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
    private final FriendsRepo friendsRepo;

    public UserPreviewController(JoinUserRepo joinUserRepo, BCryptPasswordEncoder passwordEncoder, FriendsRepo friendsRepo) {
        this.joinUserRepo = joinUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.friendsRepo = friendsRepo;
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

    @GetMapping("/myInfo/{id}")
    public String userInfo(@PathVariable Long id,
                           Model model,
                           Authentication authentication) {

        String currentUsername = authentication.getName();

        JoinUserEntity currentUser = joinUserRepo.findByUserName(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        JoinUserEntity targetUser = joinUserRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(targetUser.getUserName()) || currentUsername.equals(targetUser.getEmail());

        boolean isFriend = friendsRepo.existsBySenderAndReceiverAndStatus(currentUser, targetUser, FriendsEntity.Status.ACCEPTED)
                || friendsRepo.existsBySenderAndReceiverAndStatus(targetUser, currentUser, FriendsEntity.Status.ACCEPTED);

        model.addAttribute("user", targetUser);
        model.addAttribute("isEditable", isAdmin || isOwner);
        model.addAttribute("isFriend", isFriend);
        model.addAttribute("currentUserName", currentUsername);

        return "myInfo";
    }

    @GetMapping("/myInfo/username/{username}")
    public String userInfoByUsername(@PathVariable String username,
                                     Model model,
                                     Authentication authentication) {

        String currentUsername = authentication.getName();

        JoinUserEntity currentUser = joinUserRepo.findByUserName(currentUsername)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Current user not found"));

        JoinUserEntity targetUser = joinUserRepo.findByUserName(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Target user not found"));

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwner = currentUsername.equals(targetUser.getUserName()) || currentUsername.equals(targetUser.getEmail());

        boolean isFriend = friendsRepo.existsBySenderAndReceiverAndStatus(currentUser, targetUser, FriendsEntity.Status.ACCEPTED)
                || friendsRepo.existsBySenderAndReceiverAndStatus(targetUser, currentUser, FriendsEntity.Status.ACCEPTED);

        model.addAttribute("user", targetUser);
        model.addAttribute("isEditable", isAdmin || isOwner);
        model.addAttribute("isFriend", isFriend);
        model.addAttribute("currentUserName", currentUsername);

        return "myInfo";
    }

    @PostMapping("/myInfo/{id}/update")
    @Transactional
    public String updateInfo(@PathVariable Long id,
                             @RequestParam("userName") String newUserName,
                             @RequestParam("email") String email,
                             @RequestParam(required = false) String password,
                             Authentication authentication,
                             HttpServletRequest request) {

        String currentUsername = authentication.getName();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        JoinUserEntity user = joinUserRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        boolean isOwner = currentUsername.equals(user.getUserName()) || currentUsername.equals(user.getEmail());

        if (!isAdmin && !isOwner) {
            return "redirect:/access-denied";
        }

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

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                new CustomUserDetails(user),
                user.getPassword(),
                authentication.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        request.getSession().setAttribute(
                org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        return "redirect:/api/user/myInfo/" + user.getId() + "?success";
    }

}
