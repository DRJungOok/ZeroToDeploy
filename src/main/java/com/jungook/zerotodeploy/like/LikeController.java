package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LikeController {

	private final LikeRepo likeRepo;
	private final JoinUserRepo joinUserRepo;
	private final PostRepo postRepo;

	@PostMapping("/post/like/{id}")
	public String likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
		JoinUserEntity user = joinUserRepo.findByUserName(userDetails.getUsername())
				.orElseThrow(() -> new IllegalArgumentException("사용자 없음"));

		PostEntity post = postRepo.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("게시글 없음"));

		// 이미 좋아요를 눌렀는지 확인
		if (!likeRepo.existsByPostAndUser(post, user)) {
			LikeEntity like = LikeEntity.builder()
					.post(post)
					.user(user)
					.build();
			likeRepo.save(like);
		}

		return "redirect:/post/" + id;
	}
}
