package com.rushabh.Mini_blockchain.controller;

import com.rushabh.Mini_blockchain.service.nodeService;
import com.rushabh.Mini_blockchain.service.webSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/nodes")
public class nodeController {

    @Autowired
    private webSocketService webSocketService;

    @Autowired
    private nodeService nodeService;

    @PostMapping("/register")
    public String registerNode(@RequestBody Set<String> nodes) {
        for (String node : nodes) {
            nodeService.registerNode(node);
            webSocketService.connectToNode(node);
        }
        return "Nodes registered & connected successfully: " + nodes;
    }

}
