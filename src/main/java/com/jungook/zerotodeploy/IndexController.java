package com.jungook.zerotodeploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/history")
    public String history() {
        return "history";
    }
    @GetMapping("/notFound")
    public String notFound() {
        return "notFound";
    }
    @GetMapping("/javaSpring")
    public String javaSpring() {
        return "javaSpring";
    }
    @GetMapping("/linux")
    public String linux() {
        return "linux";
    }
    @GetMapping("/web")
    public String web() {
        return "web";
    }
    @GetMapping("/write")
    public String write() {
        return "write";
    }
}
