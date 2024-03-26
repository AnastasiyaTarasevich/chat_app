package com.nastya.chatapp.chat;

import com.nastya.chatapp.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(), true
        ).orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessage.setStatus(MessageStatus.RECEIVED);
        repository.save(chatMessage);
        return chatMessage;
    }
    public List<ChatMessage> findChatMessages(
            String senderId, String recipientId
    )
    {
        var chatId=chatRoomService.getChatRoomId(senderId,
                recipientId,
                false);
        return chatId.map(repository::findByChatId).orElse(new ArrayList<>());
    }




    public ChatMessage updateMessageStatus(String messageId, MessageStatus newStatus) {
        Optional<ChatMessage> optionalChatMessage = repository.findById(messageId);
        if (optionalChatMessage.isPresent()) {
            ChatMessage chatMessage = optionalChatMessage.get();
            chatMessage.setStatus(newStatus);
            return repository.save(chatMessage);
        } else {
            throw new RuntimeException("Message not found with ID: " + messageId);
        }
    }

    public boolean updateMessageStatus(ChatMessage message) {
        ChatMessage messageChat = repository.findById(message.getId()).orElse(null);
        if (messageChat!=null && messageChat.getStatus()!=null){
            messageChat.setStatus(MessageStatus.DELIVERED);
            repository.save(messageChat);
            return true;
        }
        return false;
    }

    public List<ChatMessage> getReceivedMessagesByUserId(String userId) {
        return repository.findBySenderIdAndStatus(userId,MessageStatus.RECEIVED);
    }

    public List<ChatMessage> getMessagesFromSender(String senderId) {
        return repository.findBySenderId(senderId);
    }
}
