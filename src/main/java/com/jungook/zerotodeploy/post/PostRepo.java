package com.jungook.zerotodeploy.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
	List<PostEntity> findByCategory(String category);
	@Query("SELECT p FROM PostEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
	List<PostEntity> searchByTitle(@Param("keyword") String keyword);
	@Query("select p from PostEntity p where lower(p.content) like lower(concat('%', :keyword, '%'))")
	List<PostEntity> searchByContent(@Param("keyword") String keyword);
	@Query("select p from PostEntity p where lower(p.title) like lower(concat('%', :keyword, '%')) or lower(p.content) like lower(concat('%', :keyword, '%'))")
	List<PostEntity> searchByTitleOrContent(@Param("keyword") String keyword);
	
	// 사용자별 게시글 조회
	@Query("SELECT p FROM PostEntity p WHERE p.author = :author")
	List<PostEntity> findByAuthor(@Param("author") String author);
	
	// 키워드 + 카테고리 검색
	@Query("SELECT p FROM PostEntity p WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND p.category = :category")
	List<PostEntity> searchByTitleOrContentAndCategory(@Param("keyword") String keyword, @Param("category") String category);
	
	// 최신 게시글 20개 조회
	@Query("SELECT p FROM PostEntity p ORDER BY p.createdAt DESC")
	List<PostEntity> findTop20ByOrderByCreatedAtDesc();
}
