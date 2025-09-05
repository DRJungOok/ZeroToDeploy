package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

      ChatEntity chat = ChatEntity.individual(roomKey, u1, u2);
      return chatRepo.save(chat);
    });
  }

  public ChatEntity getRoom(Long id) {
    return chatRepo.findById(id).orElseThrow();
  }

  @Transactional
  public ChatEntity renameRoom(Long id, String newName) {
    ChatEntity room = chatRepo.findById(id).orElseThrow();
    room.setRoomName(newName);
    return chatRepo.save(room);
  }

  /** 여러 사용자 ID로 그룹 채팅방 생성 */
  @Transactional
  public ChatEntity createGroupRoom(Set<Long> userIds, String roomName) {
    if (userIds == null || userIds.size() < 2) {
      throw new IllegalArgumentException("그룹방은 최소 2명 이상이어야 합니다.");
    }

    Set<JoinUserEntity> users = userIds.stream()
        .map(id -> joinUserRepo.findById(id).orElseThrow())
        .collect(Collectors.toSet());

    // roomKey는 참여자 userName을 정렬하여 '_'로 조인
    List<String> sortedNames = users.stream()
        .map(JoinUserEntity::getUserName)
        .sorted(String::compareToIgnoreCase)
        .toList();
    String roomKey = String.join("_", sortedNames);

    // 중복 그룹방 방지: 동일 참여자 세트 roomKey 존재 시 해당 방 재사용
    var existing = chatRepo.findByRoomKey(roomKey);
    if (existing.isPresent()) {
      return existing.get();
    }

    ChatEntity chat = new ChatEntity();
    chat.setRoomKey(roomKey);
    chat.setRoomName(roomName != null && !roomName.isBlank() ? roomName.trim() : String.join(", ", sortedNames));
    chat.setParticipants(users);

    return chatRepo.save(chat);
  }

  @Transactional(readOnly = true)
  public List<ChatEntity> listMyRooms(String userName) {
    return chatRepo.findByParticipants_UserNameOrderByIdDesc(userName);
  }

  @Transactional
  public void deleteRoom(Long roomId, String userName) {
    ChatEntity room = chatRepo.findById(roomId).orElseThrow();
    boolean isParticipant = room.getParticipants().stream()
        .anyMatch(u -> userName.equals(u.getUserName()));
    if (!isParticipant) {
      throw new IllegalArgumentException("방에 참여하고 있지 않습니다.");
    }
    chatRepo.delete(room);
  }
}
