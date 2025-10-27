package com.rushabh.Mini_blockchain.service;

import com.rushabh.Mini_blockchain.blockchain.Block;
import com.rushabh.Mini_blockchain.blockchain.Blockchain;
import com.rushabh.Mini_blockchain.blockchain.Transaction;
import com.rushabh.Mini_blockchain.blockchain.Wallet;
import com.rushabh.Mini_blockchain.repository.blockRepository;
import com.rushabh.Mini_blockchain.websocket.Broadcaster;
import com.rushabh.Mini_blockchain.websocket.nodeWebSocketHandler;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Getter
public class blockchainService {

    private final Blockchain blockchain;
    private final blockRepository blockRepository;
    private final List<Transaction> transactions = new ArrayList<>();
    private final Broadcaster broadcaster;

    public blockchainService(blockRepository blockRepository, Broadcaster broadcaster) {
        this.blockRepository = blockRepository;
        this.broadcaster = broadcaster;
        this.blockchain = new Blockchain(4); // difficulty = 4
    }

    public void addTransaction(Wallet sender, Wallet receiver, float amount) {
        Transaction transaction = new Transaction(
                Base64.getEncoder().encodeToString(sender.getPublicKey().getEncoded()),
                Base64.getEncoder().encodeToString(receiver.getPublicKey().getEncoded()),
                amount
        );

        transaction.generateSign(sender.getPrivateKey());
        transactions.add(transaction);
    }


    // inside minePendingTransactions()
    public void mineTransactions() {
        if (transactions.isEmpty()) return;

        blockchain.addBlock(new ArrayList<>(transactions));
        Block lastBlock = blockchain.getlatestBlock();
        blockRepository.save(lastBlock);

        transactions.clear();

        // Broadcast mined block to all peers instantly 🚀
       broadcaster.broadcast(lastBlock);
    }

    public boolean validateChain() {
        return blockchain.isChainValid();
    }

    public void saveChain(String filename) {
        blockchain.saveBlockchainToFile(filename);
    }

    public void loadChain(String filename) {
        blockchain.loadBlockchainFromFile(filename);
    }

    public void repairChaim() {
        blockchain.repairChain();
    }

    public void addExternalBlock(Block block) {
        if (blockchain.isChainValid()) {
            blockchain.getChain().add(block);
            blockRepository.save(block);
            System.out.println("✅ Added external block to chain.");
        } else {
            System.out.println("⚠️ Rejected invalid block.");
        }
    }

}

