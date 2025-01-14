package com.jungook.zerotodeploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/zerotodeploy")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/signUp")
    public String signUp() {
        return "SignUp";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/history")
    public String history() {
        return "history";
    }
    @GetMapping("/category")
    public String category() {
        return "category";
    }
    @GetMapping("/notFound")
    public String notFound() {
        return "notFound";
    }
    @GetMapping("/java")
    public String java() {
        return "java";
    }
    @GetMapping("/springboot")
    public String springboot() {
        return "springboot";
    }
    @GetMapping("/ubuntu")
    public String ubuntu() {
        return "ubuntu";
    }
    @GetMapping("/html")
    public String html() {
        return "html";
    }
    @GetMapping("/css")
    public String css() {
        return "css";
    }
    @GetMapping("/javascript")
    public String javascript() {
        return "javascript";
    }
    @GetMapping("/thymeleaf")
    public String thymeleaf() {
        return "thymeleaf";
    }
    @GetMapping("/write")
    public String write() {
        return "/write";
    }
}
