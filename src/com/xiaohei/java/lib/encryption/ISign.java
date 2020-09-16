package com.xiaohei.java.lib.encryption;


public interface ISign {
    /**
     * 签名
     *
     * @param text
     * @return
     */
    String sign(String text) throws Exception;

    /**
     * 签名
     *
     * @param text
     * @return
     */
    byte[] sign(byte[] text) throws Exception;

    /**
     * 验证
     *
     * @param text
     * @param signText
     * @return
     */
    boolean verify(String text, String signText) throws Exception;

    /**
     * 验证
     *
     * @param text
     * @param signText
     * @return
     */
    boolean verify(byte[] text, byte[] signText) throws Exception;
}
