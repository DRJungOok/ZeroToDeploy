package com.jungook.zerotodeploy.like;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

       private static final Logger log = LoggerFactory.getLogger(LikeController.class);

       private final LikeRepo likeRepo;
       private final JoinUserRepo joinUserRepo;
       private final PostRepo postRepo;

	@PostMapping("/post/like/{id}")
	@ResponseBody
	@Transactional
	public ResponseEntity<Map<String, Object>> toggleLike(@PathVariable("id") Long id, Authentication authentication) {

		if (authentication == null || !authentication.isAuthenticated()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		JoinUserEntity currentUser;
		Object principal = authentication.getPrincipal();

		if (principal instanceof OAuth2User oAuth2User) {
			Object emailObj = oAuth2User.getAttribute("email");
			if (emailObj == null) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			}
			String email = String.valueOf(emailObj);
			currentUser = joinUserRepo.findByEmail(email)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));
		}
		// 일반 로그인인 경우
		else {
			String username = authentication.getName();
			currentUser = joinUserRepo.findByUserName(username)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자 없음"));
		}

		PostEntity post = postRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글 없음"));

		boolean alreadyLiked = likeRepo.existsByPostAndUser(post, currentUser);
		boolean likedNow;

		if (alreadyLiked) {
			likeRepo.deleteByPostAndUser(post, currentUser);
			post.setLikeCount(post.getLikeCount() - 1);
			likedNow = false;
		} else {
			likeRepo.save(LikeEntity.builder().post(post).user(currentUser).build());
			post.setLikeCount(post.getLikeCount() + 1);
			likedNow = true;
		}

		postRepo.save(post);

		Map<String, Object> response = new HashMap<>();
		response.put("likeCount", post.getLikeCount());
		response.put("liked", likedNow);

               log.info("좋아요 POST ID: {}", post.getId());
               log.info("사용자 EMAIL 또는 USERNAME: {}", currentUser.getEmail());
               log.info("현재 좋아요 수: {}", post.getLikeCount());

		return ResponseEntity.ok(response);
	}
}
