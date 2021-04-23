package com.mettl.videoapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mettl.videoapp.api.IConnectionRepo;
import com.mettl.videoapp.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserMessagingService {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private IConnectionRepo connectionRepo;
    private UserRepository userRepository;

    public UserMessagingService(IConnectionRepo connectionRepo, UserRepository userRepository) {
        this.connectionRepo = connectionRepo;
        this.userRepository = userRepository;
    }

    public void handleUserMessage(Message message) throws JsonProcessingException {
        String msgType = message.getMsgType();
        String from = message.getFrom();
        String to = message.getTo();
        String msg = message.getMessage();
        ObjectMapper objectMapper = new ObjectMapper();
        switch (msgType) {
            case "CHECK_AVAILABILITY":
                Message res;
                if (userRepository.isAvailable(msg)) {
                    res = new Message("server", from, Boolean.toString(true), "AVAILABILITY_RESPONSE");
                } else {
                    res = new Message("server", from, Boolean.toString(false), "AVAILABILITY_RESPONSE");
                }
                connectionRepo.sendMessage(from, objectMapper.writeValueAsString(res));
                break;

            default:
                if (userRepository.isAvailable(to)) {
                    try {
                        connectionRepo.sendMessage(to, objectMapper.writeValueAsString(message));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error("{} not available while sending msg {} from {}", message.getTo(), message.getMessage(), message.getFrom());
                }
        }
    }
}
