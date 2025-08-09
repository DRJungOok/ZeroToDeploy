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
    private final FriendsService friendsService; // 추후 1:n 등에 사용
    private final JoinUserRepo joinUserRepo;     // 추후 1:n 등에 사용

    /** 친구 선택 화면 */
    @GetMapping("/room/user/{friendName}")
    public String selectType(@PathVariable String friendName, Model model) {
        model.addAttribute("friendUserName", friendName);
        return "chatSelect";
    }

    /** 1:1 채팅 시작 */
    @PostMapping("/room/individual/{friendName}")
    public String createIndividual(@PathVariable String friendName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), friendName);
        return "redirect:/chat/room/" + room.getId();
    }

    /** 채팅방 뷰 */
    @GetMapping("/room/{id}")
    public String viewRoom(@PathVariable Long id, Model model) {
        ChatEntity room = chatService.getRoom(id);
        model.addAttribute("chatRoomName", room.getRoomName());
        model.addAttribute("participants", room.getParticipants());
        model.addAttribute("roomId", room.getId());
        return "chat";
    }

    /** 바로 유저명으로 1:1 방 이동 (옵션) */
    @GetMapping("/individual/{targetUserName}")
    public String goToIndividualChat(@PathVariable String targetUserName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), targetUserName);
        return "redirect:/chat/room/" + room.getId();
    }
}
