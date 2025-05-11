package com.jungook.zerotodeploy.comment;

import com.jungook.zerotodeploy.post.PostEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(schema = "zerotodeploy", name = "comment")
public class CommentEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Long id;
	@Column(name = "content")
	private String content;
	@Column(name = "author")
	private String author;
	@Column(name = "created_date")
	private LocalDateTime createdDate;
	@ManyToOne
	private PostEntity post;
}
