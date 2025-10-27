package com.rushabh.Mini_blockchain.blockchain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.Key;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class Blockchain {

    private List<Block> chain;
    private  int difficulty;
    public List<Block> getChain() {
        return chain;
    }

    public Blockchain(int difficulty ){
        this.chain = new ArrayList<>();
        this.difficulty = difficulty;
        chain.add(genesisBlock());
    }

    public Block genesisBlock()
    {
        List<Transaction> gentx = new ArrayList<>();
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        gentx.add(new Transaction(
                Base64.getEncoder().encodeToString(wallet1.getPublicKey().getEncoded()),
                Base64.getEncoder().encodeToString(wallet2.getPublicKey().getEncoded()),
                50F
        ));
        Block genesis = new Block(gentx, "0");
        return  genesis;
    }

    public Block getlatestBlock()
    {
        return chain.get(chain.size() -1);
    }

    public void addBlock(List<Transaction> transactions)
    {
        Block newBlock = new Block(transactions, getlatestBlock().getHash());
        System.out.println("Block is Mining...");
        newBlock.mineBlock(4);
        chain.add(newBlock);
    }

    public void printChain()
    {
        for (int i = 0; i < chain.size(); i++)
        {
            Block block = chain.get(i);
            System.out.println(" ------------------------------Block : " + i + "-------------------------------");
            System.out.println(" Current Hash : " + block.getHash());
            System.out.println(" Previous Hash : " + block.getPreviousHash());
            System.out.println(" transaction in this block : ");
            for (Transaction tx : block.getTransactions())
            {
               System.out.println("   -  " + tx);
            }
            System.out.println("---------------------------------------------------------------------------------");

        }
    }

    public boolean isChainValid()
    {
        for (int i = 1; i < chain.size(); i++)
        {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            if (!currentBlock.getHash().equals(currentBlock.calculateHash()))
            {
                System.out.println(" Block : " + i + " is invalid");
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash()))
            {
                System.out.println(" Block : " + i + " is invalid");
                return false;
            }

        }
        System.out.println("Block is valid");
        return  true;
    }

    public void repairChain()
    {
        System.out.println("------------Tring to repair this Blockchain-------------");
        for (int i = 1; i < chain.size(); i++)
        {
            Block currentBlock = chain.get(i);
            Block previousBlock = chain.get(i - 1);

            currentBlock.previousHash = previousBlock.getHash();

            currentBlock.nonce = 0;
            currentBlock.hash = currentBlock.calculateHash(); // optional before mining
            System.out.println("Re-mining block " + i + "...");
            currentBlock.mineBlock(difficulty);
        }

        System.out.println("------------Chain is repaired------------");

    }

    public void saveBlockchainToFile(String filename)
    {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Key.class, new KeySerializer()) // <-- hierarchy adapter
                .setPrettyPrinting()
                .create();


        try (FileWriter writer = new FileWriter(filename))
        {
            gson.toJson(chain , writer);
            System.out.println("blockchain loaded to file" + filename);
        }
        catch (IOException e)
        {
            System.out.println("error saving the blockchain" + e.getMessage());
        }
    }

    public void loadBlockchainFromFile(String filename)
    {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Key.class, new KeySerializer()) // <-- hierarchy adapter
                .setPrettyPrinting()
                .create();


        try (FileReader reader = new FileReader(filename))
        {
            Type chainType = new TypeToken<List<Block>>(){}.getType();
            chain = gson.fromJson(reader , chainType);
            System.out.println("blockchain loaded from the file" + filename);
        }
        catch (IOException e)
        {
            System.out.println("No previous blockchain found, starting fresh");
            chain = new ArrayList<>();
        }
    }

}
