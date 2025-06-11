package com.jungook.zerotodeploy.friends;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/friends")
@RequiredArgsConstructor
public class FriendsController {

    private final FriendsService friendsService;

    @GetMapping
    public String friends(Model model) {
        return "friends";
    }

    @PostMapping("/request/{username}")
    public String sendRuest(@PathVariable String username, Authentication authentication) {
        String sender = authentication.getName();
        friendsService.sendFriendRequest(sender, username);
        return "redirect:/friends";
    }

    @PostMapping("/accept/{id}")
    public String acceptFriend(@PathVariable Long id) {
        friendsService.acceptRequest(id);
        return "redirect:/friends";
    }
}
