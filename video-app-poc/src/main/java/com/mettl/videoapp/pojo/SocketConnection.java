package com.mettl.videoapp.pojo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mettl.videoapp.api.IConnection;
import io.vertx.core.Handler;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.http.WebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

@NoArgsConstructor
@AllArgsConstructor
public class SocketConnection implements IConnection {
    static Logger logger = LoggerFactory.getLogger(SocketConnection.class);
    private ServerWebSocket serverSocket;

    @Override
    public void close() {
        try {
            serverSocket.close();
        } catch (Exception ex) {

        }
    }

    @Override
    public void sendMessage(String message) {
        serverSocket.writeTextMessage(message);
    }

    @Override
    public void registerMessageHandler(Handler<Message> messageHandler) {
        serverSocket.frameHandler(new Handler<WebSocketFrame>() {
            @Override
            public void handle(WebSocketFrame webSocketFrame) {
                String message = webSocketFrame.textData();
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                try {

                    Message msgObject = objectMapper.readValue(message, Message.class);
                    messageHandler.handle(msgObject);
                } catch (IOException e) {
                    logger.error("error in creating class",e);
                }
            }
        });
    }

    @Override
    public void registerCloseHandler(Handler<Void> messageHandler) {
        serverSocket.closeHandler(new Handler<Void>() {
            @Override
            public void handle(Void aVoid) {
                messageHandler.handle(null);
            }
        });
    }
}
