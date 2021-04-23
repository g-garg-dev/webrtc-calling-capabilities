package com.mettl.videoapp.api;

import io.vertx.core.http.ServerWebSocket;

public interface IUserCommunicationService {

    void sendMessage(String userId, String message);
}
