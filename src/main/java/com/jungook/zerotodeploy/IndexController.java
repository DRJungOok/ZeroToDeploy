package com.jungook.zerotodeploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/index")
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
    @GetMapping("/jobDetail")
    public String jobDetail() {
        return "jobDetail";
    }
    @GetMapping("/jobList")
    public String jobList() {
        return "jobList";
    }
    @GetMapping("/notFound")
    public String notFound() {
        return "notFound";
    }
    @GetMapping("/testimonial")
    public String testimonial() {
        return "testimonial";
    }
}
