package com.xiaohei.java.lib.encryption;


public interface IEncryption {
    String DES_CBC = "DES/CBC/PKCS5Padding";
    String DES = "DES";
    String AES = "AES";
    String AES_CBC = "AES/CBC/PKCS5Padding";
    String RSA = "RSA";

    /**
     * @param text
     * @return 加密
     */
    String encryption(String text) throws Exception;

    /**
     * @param text
     * @return 加密
     */
    String encryption(String text, String vector) throws Exception;

    /**
     * @param text
     * @return 加密
     */
    byte[] encryption(byte[] text) throws Exception;

    /**
     * @param text
     * @return 加密
     */
    byte[] encryption(byte[] text, byte[] vector) throws Exception;

    /**
     * @param text
     * @return 解密
     */
    String decryption(String text) throws Exception;

    /**
     * @param text
     * @return 解密
     */
    String decryption(String text, String vector) throws Exception;

    /**
     * @param text
     * @return 解密
     */
    byte[] decryption(byte[] text) throws Exception;

    /**
     * @param text
     * @return 解密
     */
    byte[] decryption(byte[] text, byte[] vector) throws Exception;
}
