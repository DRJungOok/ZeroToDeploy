package com.jungook.zerotodeploy.search;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private PostRepo postRepo;

    @GetMapping
    public String search(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "category", required = false) String category,
                        @RequestParam(name = "sortBy", required = false) String sortBy,
                        @RequestParam(name = "filter", required = false) String filter,
                        Model model) {
        
        List<PostEntity> searchResults;
        
        // 검색 조건에 따른 결과 조회
        if (keyword != null && !keyword.trim().isEmpty()) {
            if (category != null && !category.trim().isEmpty()) {
                // 키워드 + 카테고리 검색
                searchResults = postRepo.searchByTitleOrContentAndCategory(keyword, category);
            } else {
                // 키워드만으로 검색
                searchResults = postRepo.searchByTitleOrContent(keyword);
            }
        } else if (category != null && !category.trim().isEmpty()) {
            // 카테고리만으로 검색
            searchResults = postRepo.findByCategory(category);
        } else {
            // 검색 조건이 없으면 최신 게시글 조회
            searchResults = postRepo.findTop20ByOrderByCreatedAtDesc();
        }
        
        // 정렬 처리
        if ("oldest".equals(sortBy)) {
            // 오래된순: 작성일 오름차순
            searchResults.sort((a, b) -> a.getCreatedAt().compareTo(b.getCreatedAt()));
        } else if ("popular".equals(sortBy)) {
            // 인기순: 좋아요 수 내림차순
            searchResults.sort((a, b) -> Integer.compare(b.getLikeCount(), a.getLikeCount()));
        } else {
            // 기본값: 최신순 (작성일 내림차순)
            searchResults.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        }
        
        // 모델에 데이터 추가
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("filter", filter);
        model.addAttribute("resultCount", searchResults.size());
        
        // 카테고리 목록
        model.addAttribute("categories", List.of("web", "javaSpring", "linux", "etc"));
        
        return "search/results";
    }
}
