package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.friends.FriendsService;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.message.MessageService;
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
    private final MessageService messageService;

    @GetMapping("/room/user/{friendName}")
    public String selectType(@PathVariable("friendName") String friendName, Model model) {
        model.addAttribute("friendUserName", friendName);
        return "chatSelect";
    }

    @PostMapping("/room/individual/{friendName}")
    public String createChatRoom(@PathVariable("friendName") String friendName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), friendName);
        return "redirect:/chat/room/" + room.getId();
    }

    @GetMapping("/room/{id}")
    public String viewRoom(@PathVariable("id") Long id, Model model) {
        ChatEntity room = chatService.getRoom(id);
        model.addAttribute("chatRoomName", room.getRoomName());
        model.addAttribute("participants", room.getParticipants());
        model.addAttribute("roomId", room.getId());
        return "chat";
    }

    @GetMapping("/individual/{targetUserName}")
    public String goToIndividualChat(@PathVariable("targetUserName") String targetUserName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), targetUserName);
        return "redirect:/chat/room/" + room.getId();
    }

    @PostMapping("/send")
    public String sendMessage(@RequestParam("message") String message, @RequestParam("roomId") Long roomId, Authentication auth) {
        if(message != null && !message.isBlank()) {
            messageService.sendMessage(roomId, auth.getName(), message.trim());
        }

        return "redirect:/chat/room/" + roomId;
    }
}
