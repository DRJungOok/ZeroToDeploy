package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.post.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepo extends JpaRepository<LikeEntity, Long> {
	boolean existsByPostAndUser(PostEntity post, JoinUserEntity user);
	Optional<LikeEntity> findByPostAndUser(PostEntity post, JoinUserEntity user);
	void deleteByPostAndUser(PostEntity post, JoinUserEntity user);
}
