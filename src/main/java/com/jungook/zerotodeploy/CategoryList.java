package com.jungook.zerotodeploy;

import com.jungook.zerotodeploy.post.PostEntity;
import com.jungook.zerotodeploy.post.PostRepo;
import org.springframework.ui.Model;

import java.util.List;
public class CategoryList {

	PostRepo postRepo;

	public String javaString(Model model) {
		List<PostEntity> posts = postRepo.findByCategory("java&spring");
		model.addAttribute("posts", posts);
		return "java&spring";
	}
}
