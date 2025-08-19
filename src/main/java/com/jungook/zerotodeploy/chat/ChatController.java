package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.friends.FriendsService;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String createChatRoom(@PathVariable String friendName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), friendName);
        return "redirect:/chat/room/" + room.getId();
    }

    @GetMapping("/room/{id}")
    public String viewRoom(@PathVariable Long id, Model model) {
        ChatEntity room = chatService.getRoom(id);
        model.addAttribute("chatRoomName", room.getRoomName());
        model.addAttribute("participants", room.getParticipants());
        model.addAttribute("roomId", room.getId());
        return "chat";
    }

    @GetMapping("/individual/{targetUserName}")
    public String goToIndividualChat(@PathVariable String targetUserName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), targetUserName);
        return "redirect:/chat/room/" + room.getId();
    }
}
