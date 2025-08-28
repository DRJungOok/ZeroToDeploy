package com.jungook.zerotodeploy.notification;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "zerotodeploy", name = "notifications")
public class NotificationEntity {
    
    public enum NotificationType {
        FRIEND_REQUEST,    // 친구 요청
        COMMENT,           // 댓글
        LIKE,             // 좋아요
        POST_UPDATE       // 게시글 업데이트
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private JoinUserEntity recipient;
    
    @Column(name = "sender_name", nullable = false)
    private String senderName;
    
    @Column(name = "message", nullable = false)
    private String message;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;
    
    @Column(name = "related_id")
    private Long relatedId; // 관련 게시글/댓글 ID
    
    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @PrePersist
    public void setCreatedAt() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
