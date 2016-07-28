/*
 * @(#) DecryptionException.java 2013-7-17
 * 
 * Copyright 2010 NetEase.com, Inc. All rights reserved.
 */
package com.netease.hzqa.olive.newAppInterface.utils.encrypt;

/**
 * 类DecryptionException
 *
 * @author hzliuqing
 * @version 2013-7-17
 */
public class DecryptionException extends Exception {

    private static final long serialVersionUID = 2384153966440607172L;

    public DecryptionException(String msg) {
        super(msg);
    }
    
    public DecryptionException(String msg, Exception e) {
        super(msg, e);
    }
    
    public DecryptionException(Exception e) {
        super(e);
    }
    
}
