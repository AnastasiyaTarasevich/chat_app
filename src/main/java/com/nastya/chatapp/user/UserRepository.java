package com.nastya.chatapp;

import com.nastya.chatapp.user.Status;
import com.nastya.chatapp.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository <User, String> {
    List<User> findAllByStatus(Status status);
}
