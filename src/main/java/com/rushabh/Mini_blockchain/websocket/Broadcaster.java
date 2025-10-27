package com.rushabh.Mini_blockchain.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import static com.rushabh.Mini_blockchain.websocket.nodeWebSocketHandler.sessions;

@Component
@RequiredArgsConstructor
public class Broadcaster {


    public static void broadcast(Object message) {
        try {
            String msg = new ObjectMapper().writeValueAsString(message);
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
