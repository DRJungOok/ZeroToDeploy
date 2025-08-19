package com.jungook.zerotodeploy.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<MessageEntity, Long> {
	List<MessageEntity> findByChatIdOrderByIdAsc(Long chatId);
	List<MessageEntity> findByChatIdAndIdGreaterThanOrderByIdAsc(Long chatId, Long afterMessageId);

	List<MessageEntity> findByChatIdAndCreatedAfterOrderByIdAsc(Long chatId, LocalDateTime after);

}
