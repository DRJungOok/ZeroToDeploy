package com.jungook.zerotodeploy.chat;

import com.jungook.zerotodeploy.friends.FriendsService;
import com.jungook.zerotodeploy.joinMember.JoinUserRepo;
import com.jungook.zerotodeploy.message.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    // 내 채팅방 목록 페이지
    @GetMapping
    public String chatHome(Model model, Authentication auth) {
        String me = auth.getName();
        var rooms = chatService.listMyRooms(me);
        // 최근 메시지 미리보기 포함
        List<Map<String, Object>> roomDTOs = rooms.stream().map(r -> {
            var last = messageService.getLastMessage(r.getId());
            java.util.HashMap<String, Object> m = new java.util.HashMap<>();
            m.put("id", r.getId());
            m.put("roomName", r.getRoomName());
            if (last != null) {
                m.put("lastContent", last.getAttachmentUrl() != null && !last.getAttachmentUrl().isBlank() ? "[파일] " + (last.getOriginalFileName() == null ? "첨부" : last.getOriginalFileName()) : last.getContent());
                m.put("lastTime", last.getCreated());
            }
            return m;
        }).collect(Collectors.toList());
        model.addAttribute("rooms", roomDTOs);
        return "chatList";
    }

    // 내 채팅방 목록 JSON
    @GetMapping("/rooms")
    @ResponseBody
    public List<Map<String, Object>> myRooms(Authentication auth) {
        String me = auth.getName();
        return chatService.listMyRooms(me).stream().map(r -> {
            java.util.HashMap<String, Object> m = new java.util.HashMap<>();
            m.put("id", r.getId());
            m.put("roomName", r.getRoomName());
            var last = messageService.getLastMessage(r.getId());
            if (last != null) {
                m.put("lastContent", last.getAttachmentUrl() != null && !last.getAttachmentUrl().isBlank() ? "[파일] " + (last.getOriginalFileName() == null ? "첨부" : last.getOriginalFileName()) : last.getContent());
                m.put("lastTime", last.getCreated() == null ? "" : last.getCreated().toString());
            }
            return m;
        }).collect(Collectors.toList());
    }

    // 친구 목록 JSON (수락된 친구만)
    @GetMapping("/friends")
    @ResponseBody
    public List<Map<String, Object>> myFriends(Authentication auth) {
        String me = auth.getName();
        var relations = friendsService.getFriends(me);
        return relations.stream().map(rel -> {
            var other = rel.getSender().getUserName().equals(me) ? rel.getReceiver() : rel.getSender();
            java.util.HashMap<String, Object> m = new java.util.HashMap<>();
            m.put("id", other.getId());
            m.put("userName", other.getUserName());
            return m;
        }).collect(Collectors.toList());
    }

    // 그룹방 생성
    @PostMapping("/room/several")
    public String createGroupRoom(@RequestParam("userIds") Set<Long> userIds,
                                  @RequestParam(value = "roomName", required = false) String roomName,
                                  Authentication auth) {
        // 본인도 포함되도록 보장
        var me = joinUserRepo.findByUserName(auth.getName()).orElseThrow();
        userIds.add(me.getId());
        ChatEntity room = chatService.createGroupRoom(userIds, roomName);
        return "redirect:/chat/room/" + room.getId();
    }

    @PostMapping("/room/individual/{friendName}")
    public String createChatRoom(@PathVariable("friendName") String friendName, Authentication auth) {
        ChatEntity room = chatService.findOrCreateRoom(auth.getName(), friendName);
        return "redirect:/chat/room/" + room.getId();
    }

    @GetMapping("/room/{id}")
    public String viewRoom(@PathVariable("id") Long id, Model model, Authentication auth) {
        ChatEntity room = chatService.getRoom(id);
        String me = auth.getName();
        boolean allowed = room.getParticipants().stream().anyMatch(u -> me.equals(u.getUserName()));
        if (!allowed) {
            return "redirect:/chat";
        }
        model.addAttribute("chatRoomName", room.getRoomName());
        model.addAttribute("participants", room.getParticipants());
        model.addAttribute("roomId", room.getId());

        // 초기 메시지 제공 (템플릿이 기대하는 키에 맞춤: sender.userName, content, createdAt)
        var all = messageService.getAllMessages(id);
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
            @RequestParam(name = "afterId", defaultValue = "0") Long afterId,
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "from", required = false) String fromStr,
            @RequestParam(name = "to", required = false) String toStr) {
        var list = java.util.Collections.<com.jungook.zerotodeploy.message.MessageEntity>emptyList();
        if ((query != null && !query.isBlank()) || (fromStr != null && toStr != null)) {
            LocalDateTime from = null, to = null;
            if (fromStr != null && toStr != null) {
                LocalDate fromD = LocalDate.parse(fromStr);
                LocalDate toD = LocalDate.parse(toStr);
                from = fromD.atStartOfDay();
                to = toD.atTime(LocalTime.MAX);
            }
            list = messageService.searchMessages(id, query, from, to);
        } else if (afterId != null && afterId > 0) {
            list = messageService.getMessagesAfter(id, afterId);
        } else {
            list = messageService.getAllMessages(id);
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return list.stream().map(m -> {
            java.util.HashMap<String, Object> json = new java.util.HashMap<>();
            json.put("id", m.getId());
            json.put("sender", m.getSender().getUserName());
            json.put("content", m.getContent());
            json.put("attachmentUrl", m.getAttachmentUrl());
            json.put("contentType", m.getContentType());
            json.put("fileName", m.getOriginalFileName());
            json.put("createdAt", m.getCreated() == null ? "" : m.getCreated().format(fmt));
            return json;
        }).collect(Collectors.toList());
    }

    // 파일 업로드 (이미지/파일)
    @PostMapping("/room/{id}/upload")
    public String upload(@PathVariable("id") Long id,
                         @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
                         Authentication auth) throws java.io.IOException {
        if (file == null || file.isEmpty()) {
            return "redirect:/chat/room/" + id;
        }
        // 저장 경로: resources/static/uploads
        String basePath = new java.io.File("src/main/resources/static/uploads").getPath();
        java.io.File dir = new java.io.File(basePath);
        if (!dir.exists()) dir.mkdirs();

        String ext = org.springframework.util.StringUtils.getFilenameExtension(file.getOriginalFilename());
        String uuid = java.util.UUID.randomUUID().toString().replace("-", "");
        String fileName = uuid + (ext == null ? "" : ("." + ext));
        java.io.File dest = new java.io.File(dir, fileName);
        file.transferTo(dest);

        String url = "/uploads/" + fileName;
        var saved = messageService.sendAttachment(id, auth.getName(), url, file.getContentType(), file.getOriginalFilename());

        var out = new java.util.HashMap<String, Object>();
        out.put("id", saved.getId());
        out.put("sender", saved.getSender().getUserName());
        out.put("content", "");
        out.put("attachmentUrl", saved.getAttachmentUrl());
        out.put("contentType", saved.getContentType());
        out.put("fileName", saved.getOriginalFileName());
        out.put("createdAt", saved.getCreated() == null ? "" : saved.getCreated().toString());
        simpMessagingTemplate.convertAndSend("/topic/chat." + id, out);

        return "redirect:/chat/room/" + id;
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

    // 방 삭제
    @DeleteMapping("/room/{id}")
    @ResponseBody
    public Map<String, Object> deleteRoom(@PathVariable("id") Long id, Authentication auth) {
        try {
            chatService.deleteRoom(id, auth.getName());
            return java.util.Map.of("ok", true, "message", "방이 삭제되었습니다.");
        } catch (Exception e) {
            return java.util.Map.of("ok", false, "message", e.getMessage());
        }
    }
}
