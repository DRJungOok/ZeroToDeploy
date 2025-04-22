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
	public List<PostEntity> searchPosts(String keyword, String filter) {
		return switch (filter) {
			case "title" -> postRepo.searchByTitle(keyword);
			case "content" -> postRepo.searchByContent(keyword);
			default -> postRepo.searchByTitle(keyword);
		};
	}
}
