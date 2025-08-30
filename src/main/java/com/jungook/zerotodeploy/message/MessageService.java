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

	@Transactional
	public MessageEntity sendAttachment(Long chatId, String senderUserName, String url, String contentType, String originalFileName) {
		ChatEntity chat = chatRepo.findById(chatId).orElseThrow(() -> new RuntimeException("Chat not found"));
		JoinUserEntity sender = joinUserRepo.findByUserName(senderUserName).orElseThrow(() -> new RuntimeException("User not found"));

		if(chat.getParticipants().stream().noneMatch(i -> i.getId().equals(sender.getId()))) {
			throw new IllegalArgumentException("you are not in chat");
		}

		MessageEntity messageEntity = MessageEntity.builder()
				.chat(chat)
				.sender(sender)
				.content("")
				.attachmentUrl(url)
				.contentType(contentType)
				.originalFileName(originalFileName)
				.build();

		return messageRepo.save(messageEntity);
	}

	@Transactional(readOnly = true)
	public MessageEntity getLastMessage(Long chatId) {
		return messageRepo.findTopByChatIdOrderByIdDesc(chatId);
	}

	@Transactional(readOnly = true)
	public List<MessageEntity> searchMessages(Long chatId, String query, java.time.LocalDateTime from, java.time.LocalDateTime to) {
		if (query != null && !query.isBlank() && from != null && to != null) {
			return messageRepo.findByChatIdAndContentContainingIgnoreCaseAndCreatedBetweenOrderByIdAsc(chatId, query.trim(), from, to);
		} else if (query != null && !query.isBlank()) {
			return messageRepo.findByChatIdAndContentContainingIgnoreCaseOrderByIdAsc(chatId, query.trim());
		} else if (from != null && to != null) {
			return messageRepo.findByChatIdAndCreatedBetweenOrderByIdAsc(chatId, from, to);
		}
		return messageRepo.findByChatIdOrderByIdAsc(chatId);
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
