package com.jungook.zerotodeploy.comment;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@AllArgsConstructor
public class CommentController {

        private CommentRepository commentRepository;
        private PostRepo postRepo;
        private JoinUserRepo joinUserRepo;

	@PostMapping("/comment/create")
	public String createComment(@RequestParam Long postId, @RequestParam String content, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

                PostEntity post = postRepo.findById(postId).orElseThrow();
                CommentEntity comment = new CommentEntity();
                comment.setContent(content);
                JoinUserEntity writer = joinUserRepo.findByUserName(principal.getName())
                                .or(() -> joinUserRepo.findByEmail(principal.getName()))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                comment.setAuthor(writer.getUserName());
                comment.setPost(post);
		comment.setCreatedDate(LocalDateTime.now());

		commentRepository.save(comment);
		return "redirect:/post/" + postId;
	}

	@PostMapping("/comment/update")
	public String updateComment(@RequestParam("commentId") Long commentId,
															@RequestParam("content") String content) {
		CommentEntity comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		comment.setContent(content);
		commentRepository.save(comment);

		return "redirect:/post/" + comment.getPost().getId();
	}

	@GetMapping("/comment/delete/{id}")
	@PreAuthorize("isAuthenticated()")
	public String deleteComment(@PathVariable Long id, Principal principal) {
		CommentEntity comment = commentRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

                JoinUserEntity currentUser = joinUserRepo.findByUserName(principal.getName())
                                .or(() -> joinUserRepo.findByEmail(principal.getName()))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                String currentUsername = currentUser.getUserName();
		Long postId = comment.getPost().getId();

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		boolean isAdmin = auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

		if(!isAdmin && !comment.getAuthor().equals(currentUsername)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}
		commentRepository.deleteById(id);
		return "redirect:/post/" + postId;
	}

}
