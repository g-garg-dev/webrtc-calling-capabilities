package com.mettl.videoapp.verticles;

import com.mettl.videoapp.pojo.SocketConnection;
import com.mettl.videoapp.service.UserService;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class Webverticle extends AbstractVerticle {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService = new UserService();

    @Override
    public void start() {
        logger.debug("deploying verticle");
        HttpServerOptions httpOpts = new HttpServerOptions();
        httpOpts.setPemKeyCertOptions(new PemKeyCertOptions()
                .setCertPath("/Users/gaurav-garg/Desktop/selfSignedCertificate/cert.pem")
                .setKeyPath("/Users/gaurav-garg/Desktop/selfSignedCertificate/key.pem"));
        httpOpts.setSsl(true);

        httpOpts.setMaxWebsocketFrameSize(6553600);
        HttpServer server = vertx.createHttpServer(httpOpts);

        Router router = Router.router(vertx);
        router.get("/ping").handler(createPingHandler());
        router.get("/connect").handler(newConnectionHandler());



        StaticHandler staticHandler = StaticHandler.create("static").setCachingEnabled(false);
        router.route("/static/*").handler(staticHandler);
        router.route().handler(staticHandler);


        server.requestHandler(router::accept).listen(config().getInteger("http.port", 443),
                config().getString("http.host", "0.0.0.0"), httpServerAsyncResult -> {
                    if (httpServerAsyncResult.succeeded()) {
                        logger.info("Server is running on port {}", httpServerAsyncResult.result().actualPort());
                    } else {
                        logger.error("server start failed", httpServerAsyncResult.cause());
                    }
                });
    }

    private Handler<RoutingContext> createPingHandler() {
        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                String callback = routingContext.get("callback");
                StringBuffer responseBuffer = new StringBuffer();
                responseBuffer.append("Current Time on Server is: " + new Date() + "\n");
                HttpServerResponse response = routingContext.response().setStatusCode(200);
                if (callback == null) {
                    response.end(responseBuffer.toString());
                } else {
                    response.putHeader("content-type", "application/json; charset=utf-8");
                    response.end(callback + "()");
                }
            }
        };
    }

    private Handler<RoutingContext> newConnectionHandler() {
        return new Handler<RoutingContext>() {
            @Override
            public void handle(RoutingContext routingContext) {
                String userId = routingContext.request().getParam("userId");
                logger.debug("{} connect received", userId);
                if (userService.userExist(userId)) {
                    logger.error("{} alredy exist", userId);
                    routingContext.response().end("user already exist");
                    return;
                }
                ;
                ServerWebSocket socket = routingContext.request().upgrade();
                logger.debug("{} upgraded to websocket", userId);
                userService.handleNewUser(userId, new SocketConnection(socket));
            }
        };
    }
}
