package com.mettl.videoapp.repo;

import com.mettl.videoapp.api.IConnection;
import com.mettl.videoapp.api.IConnectionRepo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class BaseConnectionRepo implements IConnectionRepo {

    private Map<String, IConnection> usersConnection;

    public BaseConnectionRepo() {
        usersConnection = new ConcurrentHashMap<>();
    }

    @Override
    public void addConnection(String userId, IConnection connection) {
        usersConnection.put(userId, connection);
    }

    @Override
    public void clearUserConnection(String userId) {
        IConnection conn = usersConnection.remove(userId);
        if (conn != null) {
            conn.close();
        }
    }

    @Override
    public void sendMessage(String userId, String message) {
        usersConnection.get(userId).sendMessage(message);
    }
}
