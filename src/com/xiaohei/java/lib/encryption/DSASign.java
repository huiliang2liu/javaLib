package com.xiaohei.java.lib.encryption;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class DSASign implements ISign {
    enum DsaType {
        NONEWITHDSA("NONEWITHDSA"),
        SHA384WITHECDSA("SHA384WITHECDSA"),
        SHA224WITHDSA("SHA224WITHDSA"),
        SHA256WITHDSA("SHA256WITHDSA"),
        SHA1WITHECDSA("SHA1WITHECDSA"),
        NONEWITHECDSA("NONEWITHECDSA"),
        SHA256WITHECDSA("SHA256WITHECDSA"),
        SHA224WITHECDSA("SHA224WITHECDSA"),
        SHA512WITHECDSA("SHA512WITHECDSA"),
        SHA1WITHDSA("SHA1WITHDSA");
        private String type;

         DsaType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    private byte[] publicKey;
    private byte[] privateKey;

    public DSASign(String publicKey, String privateKey) {
        this.publicKey = Base64.decode(publicKey, 0);
        this.privateKey = Base64.decode(privateKey, 0);
    }

    @Override
    public String sign(String text) throws Exception {
        // TODO Auto-generated method stub
        return Base64.encodeToString(sign(text.getBytes()), 0);
    }

    @Override
    public byte[] sign(byte[] text) throws Exception {
        // TODO Auto-generated method stub
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(priKey);
        signature.update(text);

        return signature.sign();
    }

    @Override
    public boolean verify(String text, String signText) throws Exception {
        // TODO Auto-generated method stub
        return verify(text.getBytes(), Base64.decode(signText, 0));
    }

    @Override
    public boolean verify(byte[] text, byte[] signText) throws Exception {
        // TODO Auto-generated method stub
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);

        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initVerify(pubKey);
        signature.update(text);

        // 验证签名是否正常
        return signature.verify(signText);
    }

}
