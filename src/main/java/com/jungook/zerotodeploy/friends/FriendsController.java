package com.jungook.zerotodeploy.friends;

import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "friends" +
            "")
    public String friends(Model model, Authentication auth) {
        String username = auth.getName();
        model.addAttribute("friends", friendsService.getFriends(username));
        model.addAttribute("receivedRequests", friendsService.getReceivedRequests(username));
        model.addAttribute("sentRequests", friendsService.getSentRequests(username));
        return "friends";
    }

    @PostMapping("/request/{id}")
    public String sendRequest(@PathVariable("id") Long id, Authentication authentication) {
        friendsService.sendFriendRequest(authentication.getName(), id);
        return "redirect:/friends";
    }

    @PostMapping("/accept/{id}")
    public String acceptFriend(@PathVariable("id") Long id) {
        friendsService.acceptRequest(id);
        return "redirect:/friends";
    }

    @PostMapping("/reject/{id}")
    public String rejectRequest(@PathVariable("id") Long id) {
        friendsService.rejectRequest(id);
        return "redirect:/friends";
    }

    @PostMapping("/cancel/{id}")
    public String cancelRequest(@PathVariable("id") Long id) {
        friendsService.cancelRequest(id);
        return "redirect:/friends";
    }
}
