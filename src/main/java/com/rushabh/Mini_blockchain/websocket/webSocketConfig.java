package com.rushabh.Mini_blockchain.websocket;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class webSocketConfig implements WebSocketConfigurer {

    private final nodeWebSocketHandler nodeHandler;

    public webSocketConfig(nodeWebSocketHandler nodeHandler) {
        this.nodeHandler = nodeHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(nodeHandler, "/ws/node").setAllowedOrigins("*");
    }
}