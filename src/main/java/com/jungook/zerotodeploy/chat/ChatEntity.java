package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import jakarta.persistence.*; 
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
// Use the existing `chat` table so that foreign key constraints
// in the join table `chat_room_users` reference the correct parent table.
// The previous table name `chat_entity` caused inserts into a different
// table than the one referenced by the foreign key, leading to
// `SQLIntegrityConstraintViolationException` when saving participants.
@Table(name = "chat")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String roomKey;

    @Column(unique = true, nullable = false)
    private String roomName;

    @ManyToMany
    @JoinTable(name = "chat_room_users", joinColumns = @JoinColumn(name = "chat_room_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<JoinUserEntity> participants = new HashSet<>();

    public static ChatEntity individual(String roomKey, JoinUserEntity user1, JoinUserEntity user2) {
        Set<JoinUserEntity> users = new HashSet<>();
        users.add(user1);
        users.add(user2);
        return ChatEntity.builder().roomKey(roomKey).
         roomName(user1.getUserName() + " & " + user2.getUserName())
        .participants(users).build();
    }

    public static ChatEntity several(String roomName, Set<JoinUserEntity> users) {
        return ChatEntity.builder()
                .roomName(roomName)
                .participants(users)
                .build();
    }
}
