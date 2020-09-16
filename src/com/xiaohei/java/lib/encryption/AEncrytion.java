package com.xiaohei.java.lib.encryption;


import javax.crypto.Cipher;
import javax.crypto.NullCipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.util.Arrays;

public abstract class AEncrytion implements IEncryption {

    @Override
    public String encryption(String text) throws Exception {
        return encryption(text, null);
    }

    @Override
    public String encryption(String text, String vector) throws Exception {
        return Base64.encodeToString(
                encryption(text.getBytes(),
                        vector == null ? null : vector.getBytes()), 0);
    }

    @Override
    public byte[] encryption(byte[] text) throws Exception {
        return encryption(text, null);
    }

    @Override
    public String decryption(String text) throws Exception {
        return decryption(text, null);
    }

    @Override
    public String decryption(String text, String vector) throws Exception {
        return new String(decryption(Base64.decode(text, 0),
                vector == null ? null : vector.getBytes()));
    }

    @Override
    public byte[] decryption(byte[] text) throws Exception {
        return decryption(text, null);
    }

    @Override
    public byte[] encryption(byte[] text, byte[] vector) throws Exception {
        return deal(text, vector, true);
    }

    @Override
    public byte[] decryption(byte[] text, byte[] vector) throws Exception {
        return deal(text, vector, false);
    }

    private byte[] deal(byte[] text, byte[] vector, boolean encryption) throws Exception {
//        if (vector == null) {
//            vector = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
//        } else if (vector.length != 16)
//            vector = Arrays.copyOf(vector, 16);
        Key key = getKey(encryption);
        String transformation = getTransformation();
        if (vector != null) {
            if (transformation.equals("AES/CBC/PKCS5Padding")) {
                if (vector.length != 16)
                    vector = Arrays.copyOf(vector, 16);
            } else if (transformation.equals("DES/CBC/PKCS5Padding") || transformation.equals("DES")) {
                if (vector.length != 8)
                    vector = Arrays.copyOf(vector, 8);
            } else if (transformation.equals("DESede/ECB/PKCS5Padding"))
                vector = null;
        } else {
            if (transformation.equals("DES/CBC/PKCS5Padding") || transformation.equals("ChaCha"))
                vector = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
        }
        Cipher cipher ;
        if(transformation.equals("ECC")||transformation.equals("DSA"))
            cipher=new NullCipher();
        else
            cipher = Cipher.getInstance(transformation);
        if (vector == null)
            cipher.init(encryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key);
        else
            cipher.init(encryption ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, new IvParameterSpec(vector));
        return cipher.doFinal(text);
    }

    protected abstract Key getKey(boolean encryption);

    protected abstract String getTransformation();
}
