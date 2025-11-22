package com.jungook.zerotodeploy.statistics;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import com.jungook.zerotodeploy.comment.CommentEntity;
import com.jungook.zerotodeploy.comment.CommentRepository;
import com.jungook.zerotodeploy.like.LikeEntity;
import com.jungook.zerotodeploy.like.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private PostRepo postRepo;
    
    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private LikeRepo likeRepo;

    public Map<String, Object> getUserStatistics(String username) {
        Map<String, Object> statistics = new HashMap<>();
        
        // 기본 통계
        List<PostEntity> posts = postRepo.findByAuthor(username);
        List<CommentEntity> comments = commentRepo.findByAuthor(username);
        List<LikeEntity> likes = likeRepo.findByUserId(getUserIdByUsername(username));
        
        statistics.put("totalPosts", posts.size());
        statistics.put("totalComments", comments.size());
        statistics.put("totalLikes", likes.size());
        
        // 월별 활동 통계
        Map<String, Integer> monthlyPosts = getMonthlyActivity(posts, "posts");
        Map<String, Integer> monthlyComments = getMonthlyActivity(comments, "comments");
        
        statistics.put("monthlyPosts", monthlyPosts);
        statistics.put("monthlyComments", monthlyComments);
        
        return statistics;
    }

    private Map<String, Integer> getMonthlyActivity(List<?> items, String type) {
        Map<String, Integer> monthlyData = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        
        for (Object item : items) {
            LocalDateTime date;
            if (type.equals("posts")) {
                date = ((PostEntity) item).getCreatedAt();
            } else {
                date = ((CommentEntity) item).getCreatedDate();
            }
            
            String monthKey = date.format(formatter);
            monthlyData.put(monthKey, monthlyData.getOrDefault(monthKey, 0) + 1);
        }
        
        return monthlyData;
    }
    
    private Long getUserIdByUsername(String username) {
        // 실제 구현에서는 JoinUserRepo를 통해 사용자 ID를 조회해야 함
        // 임시로 1L 반환
        return 1L;
    }
}
