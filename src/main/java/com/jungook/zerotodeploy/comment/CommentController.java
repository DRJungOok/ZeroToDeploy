package com.jungook.zerotodeploy.comment;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class CommentController {

	@Autowired
	private CommentRepository commentRepository;

	@Autowired
	private PostRepo postRepo;

	@PostMapping("/comment/create")
	public String createComment(@RequestParam Long postId, @RequestParam String content, Principal principal) {
		if (principal == null) {
			return "redirect:/login";
		}

		PostEntity post = postRepo.findById(postId).orElseThrow();
		CommentEntity comment = new CommentEntity();
		comment.setContent(content);
		comment.setAuthor(principal.getName());
		comment.setPost(post);
		comment.setCreatedDate(LocalDateTime.now());

		commentRepository.save(comment);
		return "redirect:/post/" + postId;
	}

	@PostMapping("/comment/update")
	@Transactional
	public String updateComment(@RequestParam Long id,
															@RequestParam String content,
															Principal principal) {
		CommentEntity comment = commentRepository.findById(id).orElseThrow();
		if (!comment.getAuthor().equals(principal.getName())) {
			return "redirect:/access-denied";
		}
		comment.setContent(content);
		return "redirect:/post/" + comment.getPost().getId();
	}

	@PostMapping("/comment/delete")
	public String deleteComment(@RequestParam Long id,
															Principal principal) {
		CommentEntity comment = commentRepository.findById(id).orElseThrow();
		if (!comment.getAuthor().equals(principal.getName())) {
			return "redirect:/access-denied";
		}
		Long postId = comment.getPost().getId();
		commentRepository.deleteById(id);
		return "redirect:/post/" + postId;
	}
}
