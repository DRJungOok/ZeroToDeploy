package com.jungook.zerotodeploy.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<ChatEntity, Long> {
    Optional<ChatEntity> findByRoomKey(String roomKey);

    Optional<ChatEntity> findByRoomName(String roomName);

    // 사용자가 참여자로 포함된 채팅방 목록
    List<ChatEntity> findByParticipants_UserNameOrderByIdDesc(String userName);
}
