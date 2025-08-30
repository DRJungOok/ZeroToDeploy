package com.jungook.zerotodeploy.notification;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // 알림 목록 페이지
    @GetMapping
    public String showNotifications(@AuthenticationPrincipal JoinUserEntity user, Model model) {
        if (user == null) {
            return "redirect:/login";
        }

        var notifications = notificationService.getUserNotifications(user.getId());
        long unreadCount = notificationService.getUnreadNotificationCount(user.getId());

        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);

        return "notification/list";
    }

    // 읽지 않은 알림 수 조회 (AJAX)
    @GetMapping("/unread-count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUnreadCount(@AuthenticationPrincipal JoinUserEntity user) {
        if (user == null) {
            return ResponseEntity.ok(Map.of("count", 0));
        }

        long unreadCount = notificationService.getUnreadNotificationCount(user.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("count", unreadCount);

        return ResponseEntity.ok(response);
    }

    // 알림 읽음 처리
    @PostMapping("/{notificationId}/read")
    @ResponseBody
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "알림을 읽음 처리했습니다.");
        
        return ResponseEntity.ok(response);
    }

    // 모든 알림 읽음 처리
    @PostMapping("/mark-all-read")
    @ResponseBody
    public ResponseEntity<Map<String, String>> markAllAsRead(@AuthenticationPrincipal JoinUserEntity user) {
        if (user == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "로그인이 필요합니다."));
        }

        notificationService.markAllAsRead(user.getId());
        
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "모든 알림을 읽음 처리했습니다.");
        
        return ResponseEntity.ok(response);
    }
}
