package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "chat")
public class ChatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roomKey;

    @Column(name = "room_name", unique = true, nullable = false)
    private String roomName;

    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "chat_room_users",
        joinColumns = @JoinColumn(name = "chat_room_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<JoinUserEntity> participants = new HashSet<>();

    public static ChatEntity individual(String roomKey, JoinUserEntity user1, JoinUserEntity user2) {
        String n1 = user1 != null ? String.valueOf(user1.getUserName()) : "(unknown)";
        String n2 = user2 != null ? String.valueOf(user2.getUserName()) : "(unknown)";
        String roomName = (n1.compareToIgnoreCase(n2) <= 0) ? (n1 + " & " + n2) : (n2 + " & " + n1);

        ChatEntity chat = ChatEntity.builder()
            .roomKey(roomKey)
            .roomName(roomName)
            .build();
        chat.getParticipants().add(user1);
        chat.getParticipants().add(user2);
        return chat;
    }

    @PrePersist
    void prePersist() {
        if (roomKey == null || roomKey.isBlank()) {
            throw new IllegalStateException("roomKey must not be null/blank");
        }
        if (roomName == null || roomName.isBlank()) {
            throw new IllegalStateException("roomName must not be null/blank");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChatEntity that)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return 31; }
}
