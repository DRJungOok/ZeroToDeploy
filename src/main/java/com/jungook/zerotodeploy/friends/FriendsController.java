package com.jungook.zerotodeploy.friends;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FriendsController {
    @GetMapping("/friends")
    public String friends(Model model) {
        return "friends";
    }
}
