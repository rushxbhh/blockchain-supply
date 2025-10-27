package com.rushabh.Mini_blockchain.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class nodeService {
    private final Set<String> nodes = new HashSet<>();

    public void registerNode(String nodeUrl) {
        nodes.add(nodeUrl);
    }

    public Set<String> getNodes() {
        return nodes;
    }
}
