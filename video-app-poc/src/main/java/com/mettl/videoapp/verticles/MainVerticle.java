package com.mettl.videoapp.verticles;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {

        vertx.deployVerticle(new Webverticle());

    }
}
