package com.xiaohei.java.lib.encryption;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

class MacSign implements ISign {
    enum MacType{
        PBEWITHHMACSHA512("PBEWITHHMACSHA512"),
        PBEWITHHMACSHA224("PBEWITHHMACSHA224"),
        PBEWITHHMACSHA256("PBEWITHHMACSHA256"),
        HMACSHA384("HMACSHA384"),
        PBEWITHHMACSHA384("PBEWITHHMACSHA384"),
        HMACSHA256("HMACSHA256"),
        HMACPBESHA1("HMACPBESHA1"),
        HMACSHA224("HMACSHA224"),
        HMACMD5("HMACMD5"),
        PBEWITHHMACSHA1("PBEWITHHMACSHA1"),
        SSLMACSHA1("SSLMACSHA1"),
        HMACSHA512("HMACSHA512"),
        SSLMACMD5("SSLMACMD5"),
        HMACSHA1("HMACSHA1");

        private String type;
        private MacType(String type){
           this.type=type;
        }

        @Override
        public String toString() {
            return type;
        }
    }







    private byte[] key;
    private String type;

    public MacSign(String sKey, MacType type) {
        key = Base64.decode(sKey.getBytes(), 0);
        if(type==null)
            this.type="HMACSHA256";
        else
            this.type=type.toString();
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
