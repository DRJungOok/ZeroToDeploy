package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeRepo extends JpaRepository<LikeEntity, Long> {
	boolean existsByPostAndUser(PostEntity post, JoinUserEntity user);
	Optional<LikeEntity> findByPostAndUser(PostEntity post, JoinUserEntity user);
	void deleteByPostAndUser(PostEntity post, JoinUserEntity user);

	@Query("SELECT l FROM LikeEntity l WHERE l.post.id = :postId")
	List<LikeEntity> findByPostId(@Param("postId") Long postId);
	
	// 사용자별 좋아요 조회
	@Query("SELECT l FROM LikeEntity l WHERE l.user.id = :userId")
	List<LikeEntity> findByUserId(@Param("userId") Long userId);
}
