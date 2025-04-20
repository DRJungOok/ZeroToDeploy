package com.jungook.zerotodeploy.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PostController {

	@Autowired
	private PostRepo postRepo;

	@GetMapping("write")
	public String write() {
		return "write";
	}

	@PostMapping("write")
	public String writePost(@RequestParam String title, @RequestParam String category, @RequestParam String content) {
		PostEntity post = new PostEntity();
		post.setTitle(title);
		post.setContent(content);
		post.setCategory(category);
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

	@GetMapping("/post/{id}")
	public String detail(@PathVariable Long id, Model model) {
		PostEntity post = postRepo.findById(id).orElseThrow();
		model.addAttribute("post", post);
		return "postDetail";
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
}
