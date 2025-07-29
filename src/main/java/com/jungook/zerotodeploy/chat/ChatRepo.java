package com.jungook.zerotodeploy.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepo extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findByRoomKey(String roomKey);

    Optional<ChatEntity> findByRoomName(String roomName);
}
