package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LikeController {

	private final LikeRepo likeRepo;
	private final JoinUserRepo joinUserRepo;
	private final PostRepo postRepo;

	@PostMapping("/post/like/{id}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable Long id, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String email;

		if (authentication.getPrincipal() instanceof OAuth2User oAuth2User) {
			Map<String, Object> attributes = oAuth2User.getAttributes();
			Object responseObj = attributes.get("response");

			if (responseObj instanceof Map<?, ?> responseMap) {
				Object emailObj = responseMap.get("email");

				if (emailObj != null) {
					email = String.valueOf(emailObj);
				} else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
		} else {
			email = authentication.getName();
		}

		PostEntity post = postRepo.findById(id).orElseThrow();
		JoinUserEntity currentUser = joinUserRepo.findByEmail(email)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));

		boolean alreadyLiked = likeRepo.existsByPostAndUser(post, currentUser);
		boolean likedNow;

		if (alreadyLiked) {
			// 좋아요 취소
			likeRepo.deleteByPostAndUser(post, currentUser);
			post.setLikeCount(post.getLikeCount() - 1);
			likedNow = false;
		} else {
			// 좋아요 추가
			likeRepo.save(LikeEntity.builder().post(post).user(currentUser).build());
			post.setLikeCount(post.getLikeCount() + 1);
			likedNow = true;
		}

		postRepo.save(post);

		Map<String, Object> response = new HashMap<>();
		response.put("likeCount", post.getLikeCount());
		response.put("liked", likedNow);

		System.out.println("🧪 좋아요 POST ID: " + post.getId());
		System.out.println("🧪 사용자 EMAIL: " + currentUser.getEmail());
		System.out.println("🧪 현재 좋아요 수: " + post.getLikeCount());

		return ResponseEntity.ok(response);
	}
}
