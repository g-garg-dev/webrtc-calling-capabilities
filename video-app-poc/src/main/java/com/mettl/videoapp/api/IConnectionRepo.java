package com.mettl.videoapp.api;

public interface IConnectionRepo {

    void addConnection(String userId, IConnection connection);

    void clearUserConnection(String userId);

    void sendMessage(String userId, String message);
}
