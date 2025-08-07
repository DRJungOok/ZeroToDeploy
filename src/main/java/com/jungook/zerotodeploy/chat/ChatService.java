package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepo chatRepo;

    private final JoinUserRepo joinUserRepo;

    public ChatEntity findOrCreateRoom(String user1Name, String user2Name) {
        List<String> sorted = Arrays.asList(user1Name, user2Name);
        Collections.sort(sorted);
        String roomKey = sorted.get(0) + "_" + sorted.get(1);

      return chatRepo.findByRoomKey(roomKey).orElseGet(() -> {
        JoinUserEntity user1 = joinUserRepo.findByUserName(sorted.get(0)).orElseThrow();
        JoinUserEntity user2 = joinUserRepo.findByUserName(sorted.get(1)).orElseThrow();

        ChatEntity chat = new ChatEntity();
        chat.setRoomKey(roomKey);
        chat = chatRepo.save(chat);

        chat.getParticipants().add(user1);
        chat.getParticipants().add(user2);
        return chatRepo.save(chat);
      });
    }

    public ChatEntity createGroupRoom(String roomName, List<String> userNames) {
        return chatRepo.findByRoomName(roomName)
                .orElseGet(() -> {
                    Set<JoinUserEntity> users = userNames.stream()
                            .map(name -> joinUserRepo.findByUserName(name).orElseThrow())
                            .collect(Collectors.toSet());
                    return chatRepo.save(ChatEntity.several(roomName, users));
                });
    }

    public ChatEntity getRoom(Long roomId) {
        return chatRepo.findById(roomId).orElseThrow();
    }

}
