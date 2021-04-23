package com.mettl.videoapp;

import com.mettl.videoapp.verticles.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.LoggerFactory;

public class VideoApp {
    public static void main(String[] args) {
        System.setProperty("vertx.logger-delegate-factory-class-name", "io.vertx.core.logging.SLF4JLogDelegateFactory");
        LoggerFactory.initialise();
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle());
    }
}
