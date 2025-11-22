package com.jungook.zerotodeploy.notification;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
    // 사용자별 알림 조회 (읽지 않은 것 우선)
    @Query("SELECT n FROM NotificationEntity n WHERE n.recipient.id = :userId ORDER BY n.isRead ASC, n.createdAt DESC")
    List<NotificationEntity> findByRecipientIdOrderByIsReadAscCreatedAtDesc(@Param("userId") Long userId);
    
    // 사용자별 읽지 않은 알림 수
    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.recipient.id = :userId AND n.isRead = false")
    long countUnreadByRecipientId(@Param("userId") Long userId);
    
    // 특정 타입의 알림 조회
    List<NotificationEntity> findByRecipientAndType(JoinUserEntity recipient, NotificationEntity.NotificationType type);
}
