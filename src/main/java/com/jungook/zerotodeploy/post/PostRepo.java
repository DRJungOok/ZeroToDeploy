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
}
