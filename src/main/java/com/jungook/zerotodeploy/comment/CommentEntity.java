package com.jungook.zerotodeploy.comment;

import com.jungook.zerotodeploy.post.PostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class CommentEntity {
	@Id
	@GeneratedValue
	private Long id;
	private String content;
	private String author;
	private LocalDateTime createdDate;
	@ManyToOne
	private PostEntity post;
}
