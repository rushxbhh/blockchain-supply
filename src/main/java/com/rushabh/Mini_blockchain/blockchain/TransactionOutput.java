package com.rushabh.Mini_blockchain.blockchain;

import java.security.MessageDigest;
import java.security.PublicKey;
import java.util.Base64;

public class TransactionOutput {

    public String id;
    public PublicKey recipient;
    public Float value;
    public String parentTransactionId;

    public TransactionOutput(PublicKey recipient , Float value, String parentTransactionId)
    {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = applySha256(getStringFromKey(recipient) + Float.toString(value) + parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return publicKey.equals(recipient);
    }

    public String getStringFromKey(PublicKey key)
    {
        return Base64.getEncoder().encodeToString(key.getEncoded());
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
}
