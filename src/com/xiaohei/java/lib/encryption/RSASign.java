package com.xiaohei.java.lib.encryption;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSASign implements ISign {
    private byte[] publicKey;
    private byte[] privateKey;

    enum RsaType {
        SHA256WITHRSA("SHA256WITHRSA"),
        MD5WITHRSA("MD5WITHRSA"),
        SHA1WITHRSA("SHA1WITHRSA"),
        SHA512WITHRSA("SHA512WITHRSA"),
        MD2WITHRSA("MD2WITHRSA"),
        MD5ANDSHA1WITHRSA("MD5ANDSHA1WITHRSA"),
        SHA224WITHRSA("SHA224WITHRSA"),
        SHA384WITHRSA("SHA384WITHRSA");
        private String type;

        RsaType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    public RSASign(String publicKey, String privateKey) {
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
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance("MD5withRSA");
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
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(pubKey);
        signature.update(text);

        // 验证签名是否正常
        return signature.verify(signText);
    }

}
