package com.jungook.zerotodeploy.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
	List<CommentEntity> findByPostId(Long postId);
	
	// 사용자별 댓글 조회
	@Query("SELECT c FROM CommentEntity c WHERE c.author = :author")
	List<CommentEntity> findByAuthor(@Param("author") String author);
}