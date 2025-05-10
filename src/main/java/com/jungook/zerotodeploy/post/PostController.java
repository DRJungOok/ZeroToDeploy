package com.jungook.zerotodeploy.post;

import com.jungook.zerotodeploy.comment.CommentEntity;
import com.jungook.zerotodeploy.comment.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private PostService postService;
	@Autowired
	private CommentRepository commentRepository;

	@GetMapping("write")
	public String write() {
		return "write";
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
		model.addAttribute("posts", posts);
		return "javaSpring";
	}

	@GetMapping("/linux")
	public String linux(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("linux");
		model.addAttribute("posts", posts);
		return "linux";
	}

	@GetMapping("/web")
	public String web(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("web");
		model.addAttribute("posts", posts);
		return "web";
	}

	@GetMapping("/history")
	public String history(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("history");
		model.addAttribute("posts", posts);
		return "history";
	}

	@GetMapping("/about")
	public String about(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("about");
		model.addAttribute("posts", posts);
		return "about";
	}
	@GetMapping("/etc")
	public String etc(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("etc");
		model.addAttribute("posts", posts);
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

	@PostMapping("/comment/create")
	public String addComment(@RequestParam Long postId, @RequestParam String content, Principal principal) {
		PostEntity post = postRepo.findById(postId).orElseThrow();
		CommentEntity comment = new CommentEntity();
		comment.setContent(content);
		comment.setAuthor(principal.getName());
		comment.setPost(post);
		comment.setCreatedDate(LocalDateTime.now());

		commentRepository.save(comment);

		return "redirect:/post/" + postId;
	}

	@GetMapping("/post/{id}")
	public String postDetail(@PathVariable Long id, Model model) {
		PostEntity post = postRepo.findById(id).orElseThrow();
		model.addAttribute("post", post);
		return "postDetail";
	}

	@PostMapping("/post/like/{id}")
	public String likePost(@PathVariable Long id) {
		PostEntity post = postRepo.findById(id).orElseThrow();
		post.setLikeCount(post.getLikeCount() + 1);
		postRepo.save(post);
		return "redirect:/post/" + id;
	}
}
