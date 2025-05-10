package com.jungook.zerotodeploy.like;

import ch.qos.logback.core.joran.spi.HttpUtil;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.post.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "zerotodeploy", name = "likes")
public class LikeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private JoinUserEntity user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private PostEntity post;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();
}