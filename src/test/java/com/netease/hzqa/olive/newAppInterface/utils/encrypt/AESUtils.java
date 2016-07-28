/*
 * @(#) AESUtils.java 2013-7-16
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.netease.hzqa.olive.newAppInterface.utils.encrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * 类AESUtils
 *
 * @author hzliuqing
 * @version 2013-7-16
 * 
 */
public class AESUtils {

    private static final Logger logger = Logger.getLogger(AESUtils.class);

    public static final int KEY_LENGTH = 16;

    /**
     * AES 加密
     * 
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            logger.error("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != KEY_LENGTH) {
            logger.error("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        // "算法/模式/补码方式"
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes());

        // 此处使用BAES64做转码功能
        return Base64.encodeBase64String(encrypted);
    }

    /**
     * AES 解密
     * @param sSrc
     * @param sKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                logger.error("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != KEY_LENGTH) {
                logger.error("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// AES/CBC/NoPadding");
            IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.decodeBase64(sSrc);// 先用bAES64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original);
                return originalString;
            } catch (Exception e) {
                logger.error("AES解密出现错误", e);
                return null;
            }
        } catch (Exception ex) {
            logger.error("AES解密出现错误", ex);
            return null;
        }
    }
}