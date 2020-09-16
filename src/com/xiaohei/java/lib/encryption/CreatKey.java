package com.xiaohei.java.lib.encryption;

import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;

import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.ECFieldF2m;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.EllipticCurve;


public class CreatKey {
    private static final String DEFAULT_SEED = "0f22507a10bbddd07d8a3082122966e3";
    PublicKey publicKey;
    PrivateKey privateKey;

    public CreatKey(String algorithm) throws Exception {
        if (algorithm == null || algorithm.isEmpty())
            algorithm = "RSA";
        if(algorithm.equals("ECC")){
            BigInteger x1 = new BigInteger(
                    "2fe13c0537bbc11acaa07d793de4e6d5e5c94eee8", 16);
            BigInteger x2 = new BigInteger(
                    "289070fb05d38ff58321f2e800536d538ccdaa3d9", 16);

            ECPoint g = new ECPoint(x1, x2);

            // the order of generator
            BigInteger n = new BigInteger(
                    "5846006549323611672814741753598448348329118574063", 10);
            // the cofactor
            int h = 2;
            int m = 163;
            int[] ks = {7, 6, 3};
            ECFieldF2m ecField = new ECFieldF2m(m, ks);
            // y^2+xy=x^3+x^2+1
            BigInteger a = new BigInteger("1", 2);
            BigInteger b = new BigInteger("1", 2);

            EllipticCurve ellipticCurve = new EllipticCurve(ecField, a, b);

            ECParameterSpec ecParameterSpec = new ECParameterSpec(ellipticCurve, g,
                    n, h);
            // 公钥
            publicKey = new ECPublicKeyImpl(g, ecParameterSpec);

            BigInteger s = new BigInteger(
                    "1234006549323611672814741753598448348329118574063", 10);
            // 私钥
            privateKey = new ECPrivateKeyImpl(s, ecParameterSpec);
            return;
        }
        KeyPairGenerator keyPairGen = KeyPairGenerator
                .getInstance(algorithm);
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(DEFAULT_SEED.getBytes());
        keyPairGen.initialize(1024,secureRandom);
//        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();

    }

    /**
     * 取得私钥
     *
     * @return
     * @throws Exception
     */
    public String getPrivateKeyString()
            throws Exception {
        return Base64.encodeToString(getPrivateKeyByte(), 0);
    }

    /**
     * 取得私钥
     *
     * @return
     * @throws Exception
     */
    public byte[] getPrivateKeyByte() throws Exception {
        return privateKey.getEncoded();
    }

    /**
     * 取得公钥
     *
     * @return
     * @throws Exception
     */
    public String getPublicKeyString()
            throws Exception {
        return Base64.encodeToString(getPublicKeybyte(), 0);
    }

    /**
     * 取得公钥
     *
     * @return
     * @throws Exception
     */
    public byte[] getPublicKeybyte() throws Exception {
        return publicKey.getEncoded();
    }
}
