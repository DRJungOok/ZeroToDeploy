package com.jungook.zerotodeploy.message;


import com.jungook.zerotodeploy.chat.ChatEntity;
import com.jungook.zerotodeploy.chat.ChatRepo;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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

	@Transactional(readOnly = true)
	public List<MessageEntity> getAllMessages(Long chatId) {
		return messageRepo.findByChatIdOrderByIdAsc(chatId);
	}

	@Transactional(readOnly = true)
	public List<MessageEntity> getMessagesAfter(Long chatId, Long afterMessageId) {
		return messageRepo.findByChatIdAndIdGreaterThanOrderByIdAsc(chatId, afterMessageId);
	}

	@Transactional(readOnly = true)
	public List<MessageEntity> getMessagesAfterTime(Long chatId, LocalDateTime after) {
		return messageRepo.findByChatIdAndCreatedAfterOrderByIdAsc(chatId, after);
	}
}
