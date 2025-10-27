package com.rushabh.Mini_blockchain.blockchain;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class Block {

   // public Long id;
    public String hash;
    public String previousHash;
    public Long timeStamp;
    public Integer nonce;
    public List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Block(List<Transaction> transactions,
                 String previousHash) {
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.nonce = 0;
        this.timeStamp = System.currentTimeMillis();

        // itna yd rakho ki ye last mai hoga jab sari values add ho jaegi
        this.hash = calculateHash();
    }

    public static String applySha256(String input)
    {
        try
        {
            // java security k isse hum sha256 banaege
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // fir bytes ki array mai sab store krwa denge
            byte[] bytes = digest.digest(input.getBytes("UTF-8"));

            // ye immutale h or long k liye jada bttr hai
            StringBuilder hexString = new StringBuilder();
            for(byte b : bytes)
            {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1)
                {
                    hexString.append('0');

                }
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String calculateHash()
    {
        String merkleRoot = getMerkleTree();
        StringBuilder txData = new StringBuilder();
        for (Transaction tx : transactions) {
            txData.append(tx.getSender())
                    .append(tx.getReceiver())
                    .append(tx.getAmount());
        }
        String dataToHash = previousHash + Long.toString(timeStamp) + Integer.toString(nonce)
                + txData + merkleRoot;
        return applySha256(dataToHash);
    }

    public void mineBlock( int difficulty)
    {
        nonce = 0;
        String target = new String(new char[difficulty]).replace('\0' ,'0');
        while(!hash.substring(0 , difficulty).equals(target))
        {
            nonce++;
            hash = calculateHash();
        }
        System.out.println(" block mined : " + hash);

    }

    private List<String> getTransactionalhashes()
    {
        List<String> transactionalHashes = new ArrayList<>();
        for (Transaction transaction : this.transactions)
        {
            transactionalHashes.add(transaction.gethash());
        }
        return  transactionalHashes;
    }

    public String getMerkleTree()
    {
        List<String> currentLayer = getTransactionalhashes();
         while (currentLayer.size() > 1)
         {
             // odd wale ko handle krnge
             if (currentLayer.size() % 2 != 0)
             {
                 String lastLayer = currentLayer.get(currentLayer.size() -1);
                 currentLayer.add(lastLayer);
             }
             List<String> nextLayer = new ArrayList<>();

             for (int i = 0; i < currentLayer.size(); i += 2)
             {
                String hash1 = currentLayer.get(i);
                String hash2 = currentLayer.get(i+1);

                String combinedHash = applySha256(hash1 + hash2);
                nextLayer.add(combinedHash);
             }

             currentLayer = nextLayer;
         }
         return currentLayer.isEmpty() ? "" : currentLayer.get(0);
    }

    public boolean addTransaction(Transaction transaction) {
        if (transaction == null) return false;
        if (!transaction.verifySign()) {
            System.out.println("Transaction failed verification!");
            return false;
        }
        transactions.add(transaction);
        System.out.println("Transaction added to block.");
        return true;
    }


    public String getHash()
    {
        return hash;
    }

    public String getPreviousHash()
    {
        return previousHash;
    }

}
