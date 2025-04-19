package com.jungook.zerotodeploy.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class PostService {

	private final PostRepo postRepo;

	@Autowired
	public PostService(PostRepo postRepo) {
		this.postRepo = postRepo;
	}

	public List<PostEntity> getPostsByCategory(String category) {
		return postRepo.findByCategory(category);
	}
}
