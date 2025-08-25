package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.friends.FriendsService;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final FriendsService friendsService;
    private final JoinUserRepo joinUserRepo;
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

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
    public String viewRoom(@PathVariable("id") Long id, Model model, Authentication auth) {
        ChatEntity room = chatService.getRoom(id);
        model.addAttribute("chatRoomName", room.getRoomName());
        model.addAttribute("participants", room.getParticipants());
        model.addAttribute("roomId", room.getId());

        // 초기 메시지 제공 (템플릿이 기대하는 키에 맞춤: sender.userName, content, createdAt)
        var all = messageService.getAllMessages(id);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        List<Map<String, Object>> initialMessages = all.stream().map(m -> {
            java.util.HashMap<String, Object> outer = new java.util.HashMap<>();
            outer.put("id", m.getId());
            java.util.HashMap<String, Object> sender = new java.util.HashMap<>();
            sender.put("userName", m.getSender().getUserName());
            outer.put("sender", sender);
            outer.put("content", m.getContent());
            outer.put("createdAt", m.getCreated());
            return outer;
        }).collect(Collectors.toList());
        model.addAttribute("messages", initialMessages);

        String currentUser = auth != null ? auth.getName() : SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("me", currentUser);

        return "chat";
    }

    @PostMapping("/room/{id}/name")
    @ResponseBody
    public Map<String, Object> renameRoom(@PathVariable("id") Long id,
                                          @RequestParam("roomName") String roomName,
                                          Authentication auth) {
        if (roomName == null || roomName.isBlank()) {
            return java.util.Map.of("ok", false, "message", "방 이름을 입력하세요.");
        }
        ChatEntity room = chatService.getRoom(id);
        String me = auth.getName();
        boolean isParticipant = room.getParticipants().stream()
                .anyMatch(u -> me.equals(u.getUserName()));
        if (!isParticipant) {
            return java.util.Map.of("ok", false, "message", "권한이 없습니다.");
        }
        ChatEntity saved = chatService.renameRoom(id, roomName.trim());
        return java.util.Map.of("ok", true, "roomName", saved.getRoomName());
    }

    @GetMapping("/room/{id}/messages")
    @ResponseBody
    public List<Map<String, Object>> fetchMessages(
            @PathVariable("id") Long id,
            @RequestParam(name = "afterId", defaultValue = "0") Long afterId) {
        var list = afterId != null && afterId > 0
                ? messageService.getMessagesAfter(id, afterId)
                : messageService.getAllMessages(id);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return list.stream().map(m -> {
            java.util.HashMap<String, Object> json = new java.util.HashMap<>();
            json.put("id", m.getId());
            json.put("sender", m.getSender().getUserName());
            json.put("content", m.getContent());
            json.put("createdAt", m.getCreated() == null ? "" : m.getCreated().format(fmt));
            return json;
        }).collect(Collectors.toList());
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

    // STOMP 수신 → 저장 → 브로드캐스트
    @MessageMapping("/chat.send")
    public void handleStompSend(@Payload Map<String, Object> payload, Authentication auth) {
        Object rid = payload.get("roomId");
        Object msg = payload.get("message");
        if (rid == null || msg == null) return;
        Long roomId = Long.valueOf(String.valueOf(rid));
        String content = String.valueOf(msg);
        if (content.isBlank()) return;

        var saved = messageService.sendMessage(roomId, auth.getName(), content.trim());

        var out = new java.util.HashMap<String, Object>();
        out.put("id", saved.getId());
        out.put("sender", saved.getSender().getUserName());
        out.put("content", saved.getContent());
        out.put("createdAt", saved.getCreated() == null ? "" : saved.getCreated().toString());

        simpMessagingTemplate.convertAndSend("/topic/chat." + roomId, out);
    }
}
