package com.xiaohei.java.lib.encryption;

import com.sun.xml.internal.rngom.parse.host.Base;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class ECDSASign implements ISign{
    private static final char[] HEX = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final String SIGNALGORITHMS = "SHA256withECDSA";
    private static final String ALGORITHM = "EC";
    private static final String SECP256K1 = "secp256k1";
    private byte[] publicKey;
    private byte[] privateKey;

    public ECDSASign(String publicKey, String privateKey) {
        this.publicKey = Base64.decode(encodeHexString(publicKey.getBytes()),0);
        this.privateKey =  Base64.decode(encodeHexString(privateKey.getBytes()),0);
    }
    @Override
    public String sign(String text) throws Exception {
        return Base64.encodeToString(sign(text.getBytes()), 0);
    }

    @Override
    public byte[] sign(byte[] text) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNALGORITHMS);
        signature.initSign(priKey);
        signature.update(text);
        return signature.sign();
    }

    @Override
    public boolean verify(String text, String signText) throws Exception {
        return verify(text.getBytes(), Base64.decode(signText, 0));
    }

    @Override
    public boolean verify(byte[] text, byte[] signText) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNALGORITHMS);
        signature.initVerify(pubKey);
        signature.update(text);

        // 验证签名是否正常
        return signature.verify(signText);
    }

    public static String encodeHexString(byte[] bytes) {
        int nBytes = bytes.length;
        char[] result = new char[2 * nBytes];
        int j = 0;
        byte[] var4 = bytes;
        int var5 = bytes.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            byte aByte = var4[var6];
            result[j++] = HEX[(240 & aByte) >>> 4];
            result[j++] = HEX[15 & aByte];
        }

        return new String(result);
    }

}
