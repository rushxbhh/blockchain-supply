package com.rushabh.Mini_blockchain.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rushabh.Mini_blockchain.blockchain.Block;
import com.rushabh.Mini_blockchain.service.blockchainService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashSet;
import java.util.Set;

@Component
public class nodeWebSocketHandler extends TextWebSocketHandler {

    static final Set<WebSocketSession> sessions = new HashSet<>();
    private final blockchainService blockchainService;
    private final ObjectMapper mapper = new ObjectMapper();


    public nodeWebSocketHandler(blockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("✅ New node connected: " + session.getId());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Block receivedBlock = mapper.readValue(message.getPayload(), Block.class);
            blockchainService.addExternalBlock(receivedBlock);
            System.out.println("🧱 Received new block via WebSocket: " + receivedBlock.getHash());
        } catch (Exception e) {
            System.out.println("❌ Failed to handle WebSocket message: " + e.getMessage());
        }
    }

}
