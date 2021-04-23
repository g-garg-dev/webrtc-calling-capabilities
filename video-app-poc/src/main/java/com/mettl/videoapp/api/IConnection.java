package com.mettl.videoapp.api;

import com.mettl.videoapp.pojo.Message;
import io.vertx.core.Handler;

public interface IConnection {

    void close();

    void sendMessage(String message);

    void registerMessageHandler(Handler<Message> messageHandler);

    void registerCloseHandler(Handler<Void> messageHandler);
}
