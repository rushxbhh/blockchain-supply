package com.rushabh.Mini_blockchain.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Service
public class webSocketService {

    private final Map<String, WebSocketSession> connections = new HashMap<>();

    public void connectToNode(String nodeUrl) {
        try {
            String wsUrl = nodeUrl.replace("http", "ws") + "/ws/node";
            StandardWebSocketClient client = new StandardWebSocketClient();
            WebSocketSession session = client.execute(new TextWebSocketHandler() {}, wsUrl).get();
            connections.put(nodeUrl, session);
            System.out.println("🔗 Connected to node: " + nodeUrl);
        } catch (Exception e) {
            System.out.println("❌ Failed to connect to " + nodeUrl + ": " + e.getMessage());
        }
    }

    public Map<String, WebSocketSession> getConnections() {
        return connections;
    }
}

