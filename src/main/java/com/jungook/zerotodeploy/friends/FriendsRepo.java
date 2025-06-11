package com.jungook.zerotodeploy.friends;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepo extends JpaRepository<FriendsEntity, Long> {
    List<FriendsEntity> findBySenderOrReceiverAndStatus(JoinUserEntity sender, JoinUserEntity receiver, FriendsEntity.Status status);
    Optional<FriendsEntity> findBySenderAndReceiver(JoinUserEntity sender, JoinUserEntity receiver);

}
