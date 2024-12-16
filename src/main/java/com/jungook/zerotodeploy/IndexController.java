package com.jungook.zerotodeploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/category")
    public String category() {
        return "category";
    }
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
    @GetMapping("/job-detail")
    public String jobDetail() {
        return "job-detail";
    }
    @GetMapping("/job-list")
    public String jobList() {
        return "job-list";
    }
}
