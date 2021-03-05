package com.example.storage.modules.sys.utils;


import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ArrayUtils;

import com.example.storage.modules.sys.pojo.vo.back.TokenEntity;

/**
 * @author arvin 采用新浪微博加密算法
 */
public class TokenUtils {
    /**
     * 加密方法
     *
     * @param plaintext 要加密的数据
     * @param key       加密key
     * @param iv        加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static byte[] aesEncrypt(byte[] plaintext, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// "算法/模式/补码方式"NoPadding PkcsPadding

            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(plaintext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密方法
     *
     * @param ciphertext 要解密的数据
     * @param key        解密key
     * @param iv         解密iv
     * @return 解密的结果
     * @throws Exception
     */
    private static byte[] aesDecrypt(byte[] ciphertext, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keyspec = new SecretKeySpec(key, "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            return cipher.doFinal(ciphertext);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] hmacDigest(byte[] message, byte[] key) {
        try {
            SecretKey secretKey = new SecretKeySpec(key, "HMACSHA256");
            Mac mac = Mac.getInstance("HMACSHA256");
            mac.init(secretKey);
            return mac.doFinal(message);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TokenEntity tokenAnalysis(String strToken, byte[] aesKey, byte[] hmacKey) {
        try {
            TokenEntity token = new TokenEntity();

            byte[] raw;
            try {
                raw = Base64.getDecoder().decode(strToken);
            } catch (IllegalArgumentException e) {
                return token;
            }
            if (raw.length <= 48)
                return token;
            ByteBuffer reader = ByteBuffer.wrap(raw);
            byte[] hmac = new byte[32];
            reader.get(hmac);
            byte[] iv = new byte[16];
            reader.get(iv);
            byte[] ciphertext = new byte[reader.remaining()];
            reader.get(ciphertext);
            byte[] hmacExpect = hmacDigest(ArrayUtils.addAll(iv, ciphertext), hmacKey);
            if (!MessageDigest.isEqual(hmac, hmacExpect))
                return token;

            byte[] plaintext = aesDecrypt(ciphertext, aesKey, iv);
            reader = ByteBuffer.wrap(plaintext);

            byte[] userId = new byte[32];
            reader.get(userId);
            token.setUserId(new String(userId, "ISO-8859-1"));

            token.setTimeOut(new Date(reader.getLong()));

            byte[] uuid = new byte[32];
            reader.get(uuid);
            token.setUuid(new String(uuid, "ISO-8859-1"));

            token.setBadToken(false);

            return token;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateToken(TokenEntity token, byte[] aesKey, byte[] hmacKey) {
        byte[] plaintext;
        try {
            ByteBuffer buffer = ByteBuffer.allocate(32 + 8 + 32);
            buffer.put(token.getUserId().getBytes("ISO-8859-1"));
            buffer.putLong(token.getTimeOut().getTime());
            buffer.put(token.getUuid().getBytes("ISO-8859-1"));
            plaintext = buffer.array();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        byte[] ciphertext = aesEncrypt(plaintext, aesKey, iv);
        ciphertext = ArrayUtils.addAll(iv, ciphertext);
        byte[] hmac = hmacDigest(ciphertext, hmacKey);
        return Base64.getEncoder().encodeToString(ArrayUtils.addAll(hmac, ciphertext));
    }
}