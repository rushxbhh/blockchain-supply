package com.rushabh.Mini_blockchain.blockchain;

import lombok.Getter;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Map;

import static com.rushabh.Mini_blockchain.blockchain.Wallet.*;

@Getter
public class Transaction {

    public String transactionId;
    public String sender;
    public String receiver;
    public Float amount;
    public String signature;

    public ArrayList<TransactionInput> inputs = new ArrayList<>();
    public ArrayList<TransactionOutput> outputs = new ArrayList<>();

//    public String getSignature() {  return signature;  }
//
//    public PublicKey getSender() {
//        return sender;
//    }
//
//    public PublicKey getReceiver() {
//        return receiver;
//    }
//
//    public Float getAmount() {
//        return amount;
//    }

    public Transaction(String sender, String receiver, Float amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        //   this.signature = signature;
    }

    @Override
    public String toString() {
        return sender.substring(0, 10) + " -> " + receiver.substring(0, 10) + ": $" + amount;
    }

    public String gethash() {
        String data = sender + receiver + amount.toString() + signature;
        return Block.applySha256(data);
    }

    private String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public void generateSign(PrivateKey privateKey) {
        String data = sender + receiver + amount;
        signature = signData(data, privateKey);
    }

    public boolean verifySign() {
        String data = sender + receiver + amount;
        PublicKey senderKey = stringToPublicKey(sender);
        return verifySignature(data, signature, senderKey);
    }

    public float getInputValue() {
        float total = 0;
        for (TransactionInput i : inputs)
        {
            if (i.UTXO == null) continue;
            total += i.UTXO.value;
        }
        return  total;
    }

    public float getOutputValue() {
        float total = 0;
        for (TransactionOutput i : outputs )
        {
            total += i.value;
        }
        return  total;
    }

    public String calculateHash() {
        return Block.applySha256(sender + receiver + Float.toString(amount));
    }

    public boolean processTransaction(Map<String, TransactionOutput> utxos) {
        if (!verifySign()) {
            System.out.println("Transaction sign invalid");
            return false;
        }

        for (TransactionInput input : inputs) {
            input.UTXO = utxos.get(input.transactionOutputId);
        }

        float totalInput = getInputValue();
        if (totalInput < amount) {
            System.out.println("not enough funds" + totalInput);
            return false;
        }

        transactionId = calculateHash();

        float change = totalInput - amount;
        outputs.add(new TransactionOutput(stringToPublicKey(receiver), amount, transactionId));
        outputs.add(new TransactionOutput(stringToPublicKey(sender), change, transactionId));

        for (TransactionOutput o : outputs) {
            utxos.put(o.id, o);
        }

        for (TransactionInput i : inputs)
        {
            if (i.UTXO == null) continue;;
            utxos.remove(i.UTXO.id);
        }
        return  true;
    }

}
