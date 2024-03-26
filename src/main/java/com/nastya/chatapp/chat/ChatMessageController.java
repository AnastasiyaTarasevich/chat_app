package com.nastya.chatapp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage)
    {
        ChatMessage savedMsg=chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(), "/queue/messages",
                ChatNotification.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getSenderId())
                        .recipientId(savedMsg.getRecipientId())
                        .build()
        );
    }
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable("senderId") String senderId,
                                                              @PathVariable("recipientId") String recipientId) {
        return ResponseEntity.ok(chatMessageService.findChatMessages(senderId, recipientId));
    }
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateMessageStatus(
            @RequestBody ChatMessage message
    ) {
        boolean updatedMessage = chatMessageService.updateMessageStatus(message);
        return updatedMessage ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @GetMapping("/messages/{senderId}")
    public List<ChatMessage> getMessagesFromSender(@PathVariable String senderId) {
        return chatMessageService.getMessagesFromSender(senderId);
    }

}
