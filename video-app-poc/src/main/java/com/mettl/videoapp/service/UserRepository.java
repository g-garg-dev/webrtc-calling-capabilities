package com.mettl.videoapp.service;

import com.mettl.videoapp.pojo.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
public class UserRepository {

    private Map<String, User> users;

    public UserRepository() {
        users = new ConcurrentHashMap<>();
    }

    public void registerUser(String userId, User user) {
        users.put(userId, user);
    }

    public boolean isAvailable(String user){
        return users.containsKey(user);
    }

    public void deleteUser(String userId) {
        users.remove(userId);
    }
}
