package com.mettl.videoapp.service;

import com.mettl.videoapp.api.IConnection;
import com.mettl.videoapp.api.IConnectionRepo;
import com.mettl.videoapp.pojo.Message;
import com.mettl.videoapp.pojo.User;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private IConnectionRepo connectionRepo;

    private UserRepository userRepository;

    private UserMessagingService userMessagingService;

    public UserService() {
        connectionRepo = new WsConnectionRepo();
        userRepository = new UserRepository();
        userMessagingService = new UserMessagingService(connectionRepo, userRepository);
    }

    public boolean userExist(String userId) {
        return userRepository.isAvailable(userId);
    }

    public void handleNewUser(String userId, IConnection connection) {
        userRepository.registerUser(userId, new User(userId));
        connectionRepo.addConnection(userId, connection);
        connection.registerCloseHandler(new Handler<Void>() {
            @Override
            public void handle(Void aVoid) {
                logger.debug("{} close received", userId);
                userRepository.deleteUser(userId);
                connectionRepo.clearUserConnection(userId);
            }
        });

        connection.registerMessageHandler(new Handler<Message>() {
            @Override
            public void handle(Message message) {
                try {
                    userMessagingService.handleUserMessage(message);
                } catch (Exception ex) {
                    logger.error("error in handling messge", ex);
                }
            }
        });
    }
}
