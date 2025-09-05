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
    public String searchFromMain(@RequestParam(name = "keyword") String keyword, 
                                @RequestParam(name = "filter", required = false) String filter,
                                @RequestParam(name = "sortBy", required = false) String sortBy,
                                RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("keyword", keyword);
        
        if (filter != null && !filter.isEmpty() && !"all".equals(filter)) {
            redirectAttributes.addAttribute("filter", filter);
        }
        
        if (sortBy != null && !sortBy.isEmpty() && !"latest".equals(sortBy)) {
            redirectAttributes.addAttribute("sortBy", sortBy);
        }
        
        return "redirect:/search";
    }
}