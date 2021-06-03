package com.xiaohei.java.lib.encryption;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class MacSign implements ISign {
    private byte[] key;
    private String type;

    public MacSign(String sKey, String type) {
        key = Base64.decode(sKey.getBytes(), 0);
        if ("HmacMD5".equals(type))
            this.type = type;
        else
            this.type = "HmacSHA256";
    }

    @Override
    public String sign(String text) throws Exception {
        return Base64.encodeToString(sign(text.getBytes()), 0);
    }

    @Override
    public byte[] sign(byte[] text) throws Exception {
        Mac mac = Mac.getInstance(type);
        SecretKeySpec secret_key = new SecretKeySpec(key, type);
        mac.init(secret_key);
        return mac.doFinal(text);
    }

    @Override
    public boolean verify(String text, String signText) throws Exception {
        return sign(text).equals(signText);
    }

    @Override
    public boolean verify(byte[] text, byte[] signText) throws Exception {
        return verify(new String(text), new String(signText));
    }
}
