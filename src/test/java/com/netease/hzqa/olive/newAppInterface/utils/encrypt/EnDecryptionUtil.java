/*
 * @(#) EncryptionUtil.java 2013-7-17
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.netease.hzqa.olive.newAppInterface.utils.encrypt;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

/**
 * 类EncryptionUtil
 *
 * @author hzliuqing
 * @version 2013-7-17
 */
public class EnDecryptionUtil {
    
    private static final String KEYS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ012345678";
    
    /**
     * 创建随机的nonce
     * @param length
     * @param seed
     * @return
     */
    public static String createRandomString(int length, long seed) {
        if (length < 0) {
            return "";
        }
        Random r = new Random(seed);

        StringBuilder sBuff = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sBuff.append(KEYS.charAt(r.nextInt(KEYS.length())));
        }
        
        return sBuff.toString();
    }
    
    /**
     * 创建随机的nonce
     * @param length 长度固定16位
     * @return
     */
    private static String createNonce() {
        return createRandomString(AESUtils.KEY_LENGTH, System.currentTimeMillis());
    }
    
    /**
     * 获取加密参数
     * @return
     */
    public static Map<String,String> createPreEncryptDatas() {
    	
    	Map<String,String> map = new HashMap<String, String>();
        RSAPublicKey publicKey = RSAUtils.getDefaultPublicKey();
        map.put("modulus", new String(Hex.encodeHex(publicKey.getModulus().toByteArray())));
        map.put("pubKey", new String(Hex.encodeHex(publicKey.getPublicExponent().toByteArray())));
        map.put("nonce", EnDecryptionUtil.createNonce());
        
        return map;
    }


    /**
     * 解密：先使用rsa将encSecKey解密得到secKey，再使用AES将encText解密，AES解密过程先使用secKey作为密钥解密，再使用nonce作为密钥解密
     * 
     * @param encSecKey
     * @param encText
     * @param nonce  从session中获取nonce
     * @return
     * @throws DecryptionException
     */
    public static String decrypt(String encSecKey, String encText, String nonce) throws DecryptionException {
        try {
            String secKey = RSAUtils.decryptStringByJs(encSecKey);
            String clearText = AESUtils.decrypt(encText, secKey);
            clearText = AESUtils.decrypt(clearText, nonce);
            return clearText;
        } catch (Exception e) {
            throw new DecryptionException("解密出现错误", e);
        }
    }
}
