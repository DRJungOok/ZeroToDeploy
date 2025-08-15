package com.jungook.zerotodeploy.message;


import com.jungook.zerotodeploy.chat.ChatEntity;
import com.jungook.zerotodeploy.chat.ChatRepo;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {
	private final MessageRepo messageRepo;
	private final ChatRepo chatRepo;
	private final JoinUserRepo joinUserRepo;

	@Transactional
	public MessageEntity sendMessage(Long chatId, String senderUserName, String content) {
		ChatEntity chat = chatRepo.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
		JoinUserEntity sender = joinUserRepo.findByUserName(senderUserName).orElseThrow(() -> new RuntimeException("User not found"));

		if(chat.getParticipants().stream().noneMatch(i -> i.getId().equals(sender.getId()))) {
			throw new IllegalArgumentException("you are not in chat");
		}

		MessageEntity messageEntity = MessageEntity.builder()
				.chat(chat)
				.sender(sender)
				.content(content)
				.build();

		return messageRepo.save(messageEntity);
	}
}
