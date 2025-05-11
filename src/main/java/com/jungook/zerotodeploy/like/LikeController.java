package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LikeController {

	private final LikeRepo likeRepo;
	private final JoinUserRepo joinUserRepo;
	private final PostRepo postRepo;

	@ResponseBody
	@PostMapping(value = "/post/like/{id}", produces = "application/json")
	public Map<String, Object> likePost(@PathVariable Long id, @AuthenticationPrincipal UserDetails user) {
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
		}

		PostEntity post = postRepo.findById(id).orElseThrow();
		post.setLikeCount(post.getLikeCount() + 1);
		postRepo.save(post);

		Map<String, Object> response = new HashMap<>();
		response.put("likeCount", post.getLikeCount());
		return response;
	}
}
