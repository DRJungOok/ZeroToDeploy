package com.jungook.zerotodeploy.message;

import com.jungook.zerotodeploy.chat.ChatService;
import com.jungook.zerotodeploy.friends.FriendsEntity;
import com.jungook.zerotodeploy.friends.FriendsService;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class MessageController {
	private final FriendsService friendsService;
	private final JoinUserRepo joinUserRepo;

	@GetMapping("/room/message/{friendName}")
	public String selectType(@PathVariable("friendName") String friendName, Model model) {
		model.addAttribute("friendName", friendName);
		return "chatSelect";
	}

	@GetMapping("/room/several/{friends}")
	public String groupForm(@PathVariable("friends") String friendName, Authentication auth, Model model) {
		List<FriendsEntity> relations = friendsService.getFriends(auth.getName());
		List<JoinUserEntity> friends = relations.stream()
				.map(rel -> rel.getSender().getUserName().equals(auth.getName()) ? rel.getReceiver() : rel.getSender())
				.filter(user -> !user.getUserName().equals(friendName))
				.toList();

		JoinUserEntity target = joinUserRepo.findByUserName(friendName).orElseThrow();
		model.addAttribute("targetUser", target);
		model.addAttribute("friends", friends);
		return "groupChatForm";
	}

}
