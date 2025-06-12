package com.jungook.zerotodeploy.friends;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendsRepo extends JpaRepository<FriendsEntity, Long> {
    List<FriendsEntity> findBySenderOrReceiverAndStatus(JoinUserEntity sender, JoinUserEntity receiver, FriendsEntity.Status status);
    List<FriendsEntity> findAllByReceiverAndStatus(JoinUserEntity receiver, FriendsEntity.Status status);
    List<FriendsEntity> findAllBySenderAndStatus(JoinUserEntity receiver, FriendsEntity.Status status);
    Optional<FriendsEntity> findBySenderAndReceiver(JoinUserEntity sender, JoinUserEntity receiver);
    @Query("SELECT f FROM FriendsEntity f WHERE f.status = :status AND (f.sender = :user OR f.receiver = :user)")
    List<FriendsEntity> findAllByStatusAndUser(@Param("status") FriendsEntity.Status status,@Param("user") JoinUserEntity user);
}
