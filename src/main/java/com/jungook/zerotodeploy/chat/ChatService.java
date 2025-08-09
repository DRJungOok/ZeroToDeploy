package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

  private final ChatRepo chatRepo;
  private final JoinUserRepo joinUserRepo;

  /** 두 사용자명으로 1:1 방을 찾거나 생성 */
  @Transactional
  public ChatEntity findOrCreateRoom(String user1Name, String user2Name) {
    List<String> sorted = Arrays.asList(user1Name, user2Name);
    Collections.sort(sorted);
    String roomKey = sorted.get(0) + "_" + sorted.get(1);

    return chatRepo.findByRoomKey(roomKey).orElseGet(() -> {
      JoinUserEntity u1 = joinUserRepo.findByUserName(sorted.get(0)).orElseThrow();
      JoinUserEntity u2 = joinUserRepo.findByUserName(sorted.get(1)).orElseThrow();

      ChatEntity chat = ChatEntity.individual(roomKey, u1, u2); // ← roomName 포함 생성
      return chatRepo.save(chat);                               // ← 한 번만 save
    });
  }

  public ChatEntity getRoom(Long id) {
    return chatRepo.findById(id).orElseThrow();
  }
}
