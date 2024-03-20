package com.nastya.chatapp.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<ChatMessage> updateMessageStatus(
            @ModelAttribute ChatMessage message
    ) {
        message.setStatus(MessageStatus.DELIVERED);
        boolean updatedMessage = chatMessageService.updateMessageStatus(message);
        return (ResponseEntity<ChatMessage>) (updatedMessage ? ResponseEntity.ok() : ResponseEntity.badRequest());
    }

}
