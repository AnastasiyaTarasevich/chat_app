package com.nastya.chatapp.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findByChatId(String s);

    List<ChatMessage> findBySenderIdAndStatus(String senderId, MessageStatus messageStatus);


    Optional<ChatMessage> findById(String Id);

    List<ChatMessage> findBySenderId(String senderId);
}
