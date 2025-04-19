package com.jungook.zerotodeploy.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

		switch (category) {
			case "java&spring": return "redirect:/javaSpring";
			case "linux": return "redirect:/linux";
			case "web": return "redirect:/web";
			case "history": return "redirect:/history";
			case "about": return "redirect:/about";
			default: return "redirect:/";
		}
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
}
