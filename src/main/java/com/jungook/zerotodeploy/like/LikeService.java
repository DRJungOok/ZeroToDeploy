package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
	private final PostRepo postRepo;

	@Transactional
	public int likePost(Long postId) {
		PostEntity post = postRepo.findById(postId).orElseThrow();
		post.setLikeCount(post.getLikeCount() + 1);
		postRepo.save(post);
		return post.getLikeCount();
	}
}
