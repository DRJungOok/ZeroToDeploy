package com.jungook.zerotodeploy.post;

import com.jungook.zerotodeploy.comment.CommentRepository;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.like.LikeRepo;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@AllArgsConstructor
public class PostController {

       private static final Logger log = LoggerFactory.getLogger(PostController.class);

       private PostRepo postRepo;
       private PostService postService;
       private JoinUserRepo joinUserRepo;
       private LikeRepo likeRepo;

	@GetMapping("write")
	public String write() {
		return "write";
	}

	private List<PostEntity> summarizePosts(List<PostEntity> posts) {
		return posts.stream().map(post -> {
			PostEntity p = new PostEntity();
			p.setId(post.getId());
			p.setTitle(post.getTitle());
			p.setFileName(post.getFileName());
			p.setCreatedAt(post.getCreatedAt());
			p.setCategory(post.getCategory());

			String plain = post.getContent().replaceAll("<[^>]*>", "");
			p.setContent(plain.length() > 100 ? plain.substring(0, 100) + "..." : plain);

			return p;
		}).toList();
	}

	@PostMapping("write")
	public String writePost(@RequestParam String title,
													@RequestParam String category,
													@RequestParam String content,
													@RequestParam MultipartFile[] files) throws IOException {

		String uploadPath;
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			uploadPath = new File("src/main/resources/static/uploads").getAbsolutePath(); // Windows
		} else if (os.contains("mac")) {
			uploadPath = new File("/Users/kimjungook/uploads").getAbsolutePath(); // macOS
		} else {
			uploadPath = "/home/ubuntu/uploads/"; // Linux
		}

		File dir = new File(uploadPath);
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				throw new IOException("Failed create upload directory: " + uploadPath);
			}
		}

		PostEntity post = new PostEntity();
		post.setTitle(title);
		post.setContent(content);
		post.setCategory(category);

		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				String fileName = file.getOriginalFilename();
				file.transferTo(new File(uploadPath + File.separator + fileName));
				post.setFileName(fileName);
			}
		}
		postRepo.save(post);

		return switch (category) {
			case "javaSpring" -> "redirect:/javaSpring";
			case "linux" -> "redirect:/linux";
			case "web" -> "redirect:/web";
			case "history" -> "redirect:/history";
			case "about" -> "redirect:/about";
			case "etc" -> "redirect:/etc";
			default -> "redirect:/";
		};
	}

	@GetMapping("/api/search")
	@ResponseBody
	public List<PostEntity> searchApi(@RequestParam String keyword, @RequestParam(defaultValue = "all") String filter) {
		return postService.searchPosts(keyword, filter);
	}

	@GetMapping("/javaSpring")
	public String javaSpring(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("java&spring");
		model.addAttribute("posts", summarizePosts(posts));
		return "javaSpring";
	}

	@GetMapping("/linux")
	public String linux(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("linux");
		model.addAttribute("posts", summarizePosts(posts));
		return "linux";
	}

	@GetMapping("/web")
	public String web(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("web");
		model.addAttribute("posts", summarizePosts(posts));
		return "web";
	}

	@GetMapping("/history")
	public String history(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("history");
		model.addAttribute("posts", summarizePosts(posts));
		return "history";
	}

	@GetMapping("/about")
	public String about(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("about");
		model.addAttribute("posts", summarizePosts(posts));
		return "about";
	}

	@GetMapping("/etc")
	public String etc(Model model, @RequestParam(required = false) Long id) {
		List<PostEntity> posts = postRepo.findByCategory("etc");
		model.addAttribute("posts", summarizePosts(posts));
		return "etc";
	}

	@PostMapping("/post/update/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String update(@PathVariable Long id, @RequestParam String title, @RequestParam String content) {
		PostEntity post = postRepo.findById(id).orElseThrow();
		post.setTitle(title);
		post.setContent(content);
		postRepo.save(post);
		String category =  post.getCategory();
		return "redirect:/" + toCategoryPath(category);
	}

	@GetMapping("/post/delete/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public String delete(@PathVariable Long id) {
		PostEntity post = postRepo.findById(id).orElseThrow();
		String category = post.getCategory();

		postRepo.deleteById(id);
		return "redirect:/" + toCategoryPath(category);
	}

	private String toCategoryPath(String category) {
		return switch (category) {
			case "java&spring" -> "javaSpring";
			case "linux" -> "linux";
			case "web" -> "web";
			case "history" -> "history";
			case "about" -> "about";
			case "etc" -> "etc";
			default -> "/";
		};
	}

	@GetMapping("/post/{id}")
	public String detailPost(@PathVariable Long id, Model model, Authentication authentication) {
		PostEntity post = postRepo.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 게시글 없음"));

		if (authentication != null && authentication.isAuthenticated()) {
			String username = authentication.getName();
			JoinUserEntity user = joinUserRepo.findByUserName(username).orElse(null);

			if (user != null && likeRepo.existsByPostAndUser(post, user)) {
				post.setLikedByCurrentUser(true);
			}
		}
		model.addAttribute("post", post);
		return "postDetail";
	}

	@PostMapping("/post/write")
	public String writePost(PostEntity post) {
		PostEntity savedPost = postService.savePost(post);
               log.info("🔥 savedPost id: {}", savedPost.getId());
               return "redirect:/post/" + savedPost.getId();
       }

}
