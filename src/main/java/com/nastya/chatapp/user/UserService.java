package com.nastya.chatapp.user;

import com.nastya.chatapp.user.Status;
import com.nastya.chatapp.user.User;
import com.nastya.chatapp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public void disconnect(User user) {
        var storedUser=repository.findById(user.getNickName())
                .orElse(null);
        if(storedUser!=null)
        {
            storedUser.setStatus(Status.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }

    public List<User> findOfflineUsers() {
        return repository.findAllByStatus(Status.OFFLINE);
    }
}
