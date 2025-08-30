package com.jungook.zerotodeploy;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    // 메인 페이지에서 검색 시 검색 페이지로 리다이렉트
    @GetMapping("/main-search")
    public String searchFromMain(@RequestParam String keyword, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("keyword", keyword);
        return "redirect:/search";
    }
}