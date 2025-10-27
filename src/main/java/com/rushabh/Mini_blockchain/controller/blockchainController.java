package com.rushabh.Mini_blockchain.controller;

import com.rushabh.Mini_blockchain.blockchain.Blockchain;
import com.rushabh.Mini_blockchain.blockchain.Wallet;
import com.rushabh.Mini_blockchain.service.blockchainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/blockchain")
public class blockchainController {

    @Autowired
    private blockchainService blockchainService;

    private Wallet nodeWallet = new Wallet(); // node’s own wallet

    @PostMapping("/transaction")
    public String addTransaction(@RequestParam String receiverPublicKey, @RequestParam float amount) {
        // You could later map public keys to wallets
        Wallet receiver = new Wallet();
        blockchainService.addTransaction(nodeWallet, receiver, amount);
        return "Transaction added successfully!";
    }

    @GetMapping("/mine")
    public String mineBlock() {
        blockchainService.mineTransactions();
        return "Block mined successfully!";
    }

    @GetMapping("/validate")
    public String validate() {
        boolean valid = blockchainService.validateChain();
        return valid ? "✅ Blockchain is valid!" : "❌ Blockchain is invalid!";
    }

    @GetMapping("/chain")
    public Blockchain getFullChain() {
        return blockchainService.getBlockchain();
    }

    @GetMapping("/repair")
    public String repair() {
        blockchainService.repairChaim();
        return "chain is successfully repaired";
    }
}


