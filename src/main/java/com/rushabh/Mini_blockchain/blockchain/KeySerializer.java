package com.rushabh.Mini_blockchain.blockchain;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeySerializer implements JsonSerializer<Key> , JsonDeserializer<Key> {
    @Override
    public Key deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try
        {
            byte[] decoded = Base64.getDecoder().decode(jsonElement.getAsString());
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            if (type.getTypeName().contains("PublicKey"))
            {
                X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);
                return keyFactory.generatePublic(keySpec);
            }
            else
            {
                PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);
                return keyFactory.generatePrivate(keySpec);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement serialize(Key key, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(Base64.getEncoder().encodeToString(key.getEncoded()));
    }
}
