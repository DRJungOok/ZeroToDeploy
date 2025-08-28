package com.jungook.zerotodeploy.statistics;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import com.jungook.zerotodeploy.comment.CommentEntity;
import com.jungook.zerotodeploy.comment.CommentRepository;
import com.jungook.zerotodeploy.like.LikeEntity;
import com.jungook.zerotodeploy.like.LikeRepo;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private PostRepo postRepo;
    
    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private LikeRepo likeRepo;
    
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public String showDashboard(@AuthenticationPrincipal JoinUserEntity user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        // 사용자 활동 통계 수집
        List<PostEntity> userPosts = postRepo.findByAuthor(user.getUserName());
        List<CommentEntity> userComments = commentRepo.findByAuthor(user.getUserName());
        List<LikeEntity> userLikes = likeRepo.findByUserId(user.getId());
        
        // 통계 데이터 모델에 추가
        model.addAttribute("totalPosts", userPosts.size());
        model.addAttribute("totalComments", userComments.size());
        model.addAttribute("totalLikes", userLikes.size());
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("userComments", userComments);
        
        // 상세 통계 서비스 호출
        Map<String, Object> detailedStats = statisticsService.getUserStatistics(user.getUserName());
        model.addAttribute("monthlyPosts", detailedStats.get("monthlyPosts"));
        model.addAttribute("monthlyComments", detailedStats.get("monthlyComments"));
        
        return "statistics/dashboard";
    }
}
