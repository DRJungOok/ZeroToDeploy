package com.jungook.zerotodeploy.post;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostController {
	public String writePost(@RequestParam String title, @RequestParam String category, @RequestParam String content, Model model) {
		PostEntity post = new PostEntity();
		post.setTitle(title);
		post.setContent(content);
		post.setCategory(category);
		return "writePost";
	}
}
