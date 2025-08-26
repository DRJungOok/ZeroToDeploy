package com.jungook.zerotodeploy.message;

import com.jungook.zerotodeploy.chat.ChatEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message")

public class MessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(nullable = false)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "chat_id", nullable = false)
	private ChatEntity chat;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "sender_id", nullable = false)
	private JoinUserEntity sender;

	@Column(nullable = false, length = 2000)
	private String content;

	// 첨부 파일(이미지/파일) 지원
	@Column(name = "attachment_url")
	private String attachmentUrl;

	@Column(name = "content_type")
	private String contentType;

	@Column(name = "file_name")
	private String originalFileName;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime created;
}
