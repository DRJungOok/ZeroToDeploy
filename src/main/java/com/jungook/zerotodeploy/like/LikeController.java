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
	public String likePost(@PathVariable Long id) {
		PostEntity post = postRepo.findById(id).orElseThrow();
		post.setLikeCount(post.getLikeCount() + 1);
		postRepo.save(post);
		return "redirect:/post/" + id;
	}
}
