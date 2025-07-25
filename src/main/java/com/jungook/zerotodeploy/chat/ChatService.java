package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepo chatRepo;

    private JoinUserRepo joinUserRepo;

    public ChatEntity findOrCreateRoom(String user1Name, String user2Name) {
        List<String> sorted = Arrays.asList(user1Name, user2Name);
        Collections.sort(sorted);
        String roomKey = sorted.get(0) + "_" + sorted.get(1);
        return chatRepo.findByRoomKey(roomKey)
                .orElseGet(
                        ()-> {
                            JoinUserEntity user1 = joinUserRepo.findByUserName(sorted.get(0)).orElseThrow();
                            JoinUserEntity user2 = joinUserRepo.findByUserName(sorted.get(1)).orElseThrow();

                            return chatRepo.save(ChatEntity.individual(roomKey, user1, user2));
                        }
                );
    }

}
