package com.jungook.zerotodeploy.friends;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "zerotodeploy", name = "friend_entity")
public class FriendsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private JoinUserEntity sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private JoinUserEntity receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    public enum Status {
        REQUESTED, ACCEPTED, REJECTED
    }
}
