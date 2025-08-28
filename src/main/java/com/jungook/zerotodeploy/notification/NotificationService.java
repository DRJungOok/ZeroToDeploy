package com.jungook.zerotodeploy.notification;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.comment.CommentEntity;
import com.jungook.zerotodeploy.like.LikeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 친구 요청 알림 생성
    public void createFriendRequestNotification(JoinUserEntity recipient, String senderName) {
        NotificationEntity notification = NotificationEntity.builder()
                .recipient(recipient)
                .senderName(senderName)
                .message(senderName + "님이 친구 요청을 보냈습니다.")
                .type(NotificationEntity.NotificationType.FRIEND_REQUEST)
                .build();
        
        notificationRepository.save(notification);
    }

    // 댓글 알림 생성
    public void createCommentNotification(JoinUserEntity postAuthor, String commenterName, Long postId) {
        if (postAuthor.getUserName().equals(commenterName)) {
            return; // 본인이 댓글을 달면 알림 생성하지 않음
        }
        
        NotificationEntity notification = NotificationEntity.builder()
                .recipient(postAuthor)
                .senderName(commenterName)
                .message(commenterName + "님이 회원님의 게시글에 댓글을 달았습니다.")
                .type(NotificationEntity.NotificationType.COMMENT)
                .relatedId(postId)
                .build();
        
        notificationRepository.save(notification);
    }

    // 좋아요 알림 생성
    public void createLikeNotification(JoinUserEntity postAuthor, String likerName, Long postId) {
        if (postAuthor.getUserName().equals(likerName)) {
            return; // 본인이 좋아요를 누르면 알림 생성하지 않음
        }
        
        NotificationEntity notification = NotificationEntity.builder()
                .recipient(postAuthor)
                .senderName(likerName)
                .message(likerName + "님이 회원님의 게시글을 좋아합니다.")
                .type(NotificationEntity.NotificationType.LIKE)
                .relatedId(postId)
                .build();
        
        notificationRepository.save(notification);
    }

    // 사용자별 알림 조회
    public List<NotificationEntity> getUserNotifications(Long userId) {
        return notificationRepository.findByRecipientIdOrderByIsReadAscCreatedAtDesc(userId);
    }

    // 읽지 않은 알림 수 조회
    public long getUnreadNotificationCount(Long userId) {
        return notificationRepository.countUnreadByRecipientId(userId);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    // 모든 알림 읽음 처리
    public void markAllAsRead(Long userId) {
        List<NotificationEntity> unreadNotifications = notificationRepository
                .findByRecipientIdOrderByIsReadAscCreatedAtDesc(userId)
                .stream()
                .filter(notification -> !notification.isRead())
                .toList();
        
        unreadNotifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }
}
