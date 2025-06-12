package com.jungook.zerotodeploy.friends;

import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendsService {

    private final FriendsRepo friendsRepo;
    private final JoinUserRepo joinUserRepo;

    public void sendFriendRequest(String senderUsername, String receiverUsername) {
        JoinUserEntity sender = joinUserRepo.findByUserName(senderUsername).orElseThrow();
        JoinUserEntity receiver = joinUserRepo.findByUserName(receiverUsername).orElseThrow();

        if(friendsRepo.findBySenderAndReceiver(sender, receiver).isEmpty()) {
            FriendsEntity friendsEntity = FriendsEntity
                    .builder()
                    .sender(sender)
                    .receiver(receiver)
                    .status(FriendsEntity.Status.REQUESTED)
                    .build();

            friendsRepo.save(friendsEntity);
        }
    }

    public void acceptRequest(Long requestId) {
        FriendsEntity request = friendsRepo.findById(requestId).orElseThrow();
        request.setStatus(FriendsEntity.Status.ACCEPTED);
        friendsRepo.save(request);
    }

    public void rejectRequest(Long requestId) {
        friendsRepo.deleteById(requestId);
    }

    public void cancelRequest(Long requestId) {
        friendsRepo.deleteById(requestId);
    }

    public List<FriendsEntity> getFriends(String username) {
        JoinUserEntity user = joinUserRepo.findByUserName(username).orElseThrow();
        return friendsRepo.findBySenderOrReceiverAndStatus(user, user, FriendsEntity.Status.ACCEPTED);
    }

    public List<FriendsEntity> getReceivedRequests(String username) {
        JoinUserEntity user = joinUserRepo.findByUserName(username).orElseThrow();
        return friendsRepo.findAllByReceiverAndStatus(user, FriendsEntity.Status.REQUESTED);
    }

    public List<FriendsEntity> getSentRequests(String username) {
        JoinUserEntity user = joinUserRepo.findByUserName(username).orElseThrow();
        return friendsRepo.findAllBySenderAndStatus(user, FriendsEntity.Status.REQUESTED);
    }
}
