package com.jungook.zerotodeploy.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepo extends JpaRepository<PostEntity, Long> {
	List<PostEntity> findByCategory(String category);
	List<PostEntity> findByTitle(String title);
	List<PostEntity> findByCategoryAndTitle(String category, String title);
}
