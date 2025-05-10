package com.jungook.zerotodeploy.post;

import com.jungook.zerotodeploy.comment.CommentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table( schema = "zerotodeploy", name = "post_entity")
public class PostEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "title")
	private String title;
	@Column(name = "content")
	private String content;
	@Column(name = "category")
	private String category;
	private LocalDateTime createdAt = LocalDateTime.now();
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "like_count")
	private int likeCount = 0;

	@OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
	private List<CommentEntity> comments = new ArrayList<>();
}
