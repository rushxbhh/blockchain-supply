package com.rushabh.Mini_blockchain.blockchain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class Wallet {

    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    private void generateKeyPair()  {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            ECGenParameterSpec spec = new ECGenParameterSpec("secp256r1");
            keyPairGenerator.initialize(spec , new SecureRandom());
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public static String keyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static PublicKey stringToPublicKey(String keyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(keyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static PrivateKey stringToPrivateKey(String keyStr) {
        try {
            byte[] bytes = Base64.getDecoder().decode(keyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static  String signData(String data, PrivateKey privateKey) {
        try
        {
            Signature ecdsaSign = Signature.getInstance("SHA256withECDSA");
            ecdsaSign.initSign(privateKey);
            ecdsaSign.update(data.getBytes());
            byte[] signature = ecdsaSign.sign();
            return Base64.getEncoder().encodeToString(signature);
        }
        catch (Exception e)
        {
            throw  new RuntimeException(e);
        }
    }

    public static boolean verifySignature(String data, String signature, PublicKey publicKey) {
        try {
            Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA");
            ecdsaVerify.initVerify(publicKey);
            ecdsaVerify.update(data.getBytes());
            byte[] decodeSign = Base64.getDecoder().decode(signature);
            return ecdsaVerify.verify(decodeSign);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public String getAddress() {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA256");
            byte[] hash = digest.digest(publicKey.getEncoded());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}