package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.friends.FriendsEntity;
import com.jungook.zerotodeploy.friends.FriendsService;
import com.jungook.zerotodeploy.joinMember.JoinUserEntity;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final FriendsService friendsService;
    private final JoinUserRepo joinUserRepo;

    @GetMapping("/room/user/{friendName}")
    public String selectType(@PathVariable String friendName, Model model) {
        model.addAttribute("friendUserName", friendName);
        return "chatSelect";
    }

    @PostMapping("/room/individual/{friendName}")
    public String createIndividual(@PathVariable String friendName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), friendName);

        return "redirect:/chat/room/" + room.getId();
    }

    @GetMapping("/room/several/{friendName}")
    public String groupForm(@PathVariable String friendName, Authentication auth, Model model) {
        List<FriendsEntity> relations = friendsService.getFriends(auth.getName());
        List<JoinUserEntity> friends = relations.stream()
                .map(rel -> rel.getSender().getUserName().equals(auth.getName()) ? rel.getReceiver() : rel.getSender())
                .filter(user -> !user.getUserName().equals(friendName))
                .toList();

        JoinUserEntity target = joinUserRepo.findByUserName(friendName).orElseThrow();
        model.addAttribute("targetUser", target);
        model.addAttribute("friends", friends);
        return "groupChatForm";
    }

    @PostMapping("/room/several")
    public String createGroup(@RequestParam String roomName,
                              @RequestParam List<Long> userIds,
                              Authentication auth) {
        Set<String> names = userIds.stream()
                .map(id -> joinUserRepo.findById(id).orElseThrow())
                .map(JoinUserEntity::getUserName)
                .collect(Collectors.toSet());
        names.add(auth.getName());
        ChatEntity room = chatService.createGroupRoom(roomName, List.copyOf(names));
        return "redirect:/chat/room/" + room.getId();
    }

    @GetMapping("/room/{id}")
    public String viewRoom(@PathVariable Long id, Model model) {
        ChatEntity room = chatService.getRoom(id);
        model.addAttribute("chatRoomName", room.getRoomName());
        return "chat";
    }

    @GetMapping("/individual/{targetUserName}")
    public String goToIndividualChat(@PathVariable String targetUserName, Principal principal, Model model) {
        String myUserName = principal.getName();
        ChatEntity chatRoom = chatService.findOrCreateRoom(myUserName, targetUserName);

        model.addAttribute("room", chatRoom);
        model.addAttribute("participants", chatRoom.getParticipants());

        return "chat";
    }
}
