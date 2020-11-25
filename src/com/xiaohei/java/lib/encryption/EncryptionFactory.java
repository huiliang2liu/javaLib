package com.xiaohei.java.lib.encryption;


//import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.security.ec.ECKeyFactory;

import javax.crypto.KeyGenerator;
import javax.crypto.NullCipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.Key;
import java.security.KeyFactory;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class EncryptionFactory {
    private static final String DES_CBC = "DES/CBC/PKCS5Padding";
    private static final String DES3_CBC = "DESede/ECB/PKCS5Padding";
    public static final String IDEA_CBC = "IDEA/ECB/ISO10126Padding";
    private static final String DES = "DES";
    private static final String DES3 = "DESede";
    private static final String AES = "AES";
    public static final String IDEA = "IDEA";
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";
    private static final String RSA = "RSA";

    private static IEncryption createSymmetry(String transformation, Key key) {
        return (IEncryption) Proxy.newProxyInstance(EncryptionFactory.class.getClassLoader(), new Class[]{IEncryption.class}, new InvocationHandler() {
            IEncryption encryption = new AEncrytion() {
                @Override
                protected Key getKey(boolean encryption) {
                    return key;
                }

                @Override
                protected String getTransformation() {
                    return transformation;
                }
            };

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(encryption, args);
            }
        });
    }

    //加密的数据长度必须是16的整数倍
    public static IEncryption AES(String key) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(AES);
        kgen.init(128, new SecureRandom(key.getBytes()));
        return createSymmetry(AES_CBC, new SecretKeySpec(kgen.generateKey().getEncoded(), AES));
    }

    private final static String MODE_KEY = "01234567";

    public static IEncryption DES(String key) throws Exception {
        int mode = key.length() % 8;
        key = mode == 0 ? key : key
                + MODE_KEY.substring(0, 8 - mode);
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
//            // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
//            // 将DESKeySpec对象转换成SecretKey对象
        System.out.println(keyFactory.generateSecret(desKey).getClass().getName());
        return createSymmetry(DES_CBC, keyFactory.generateSecret(desKey));
    }

    public static IEncryption DES1(String key) throws Exception {
        int mode = key.length() % 8;
        key = mode == 0 ? key : key
                + MODE_KEY.substring(0, 8 - mode);
        DESKeySpec desKey = new DESKeySpec(key.getBytes());
//            // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
//            // 将DESKeySpec对象转换成SecretKey对象
        System.out.println(keyFactory.generateSecret(desKey).getClass().getName());
        return createSymmetry(DES, keyFactory.generateSecret(desKey));
    }

    public static IEncryption DES3(String key) throws Exception {
        key = getkey(key, 24);
        DESedeKeySpec desKey = new DESedeKeySpec(key.getBytes());
//            // 创建一个密匙工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES3);
        return createSymmetry(DES3_CBC, keyFactory.generateSecret(desKey));
    }

    public static IEncryption Blowfish(String key) {
//        Security.addProvider(new BouncyCastleProvider());
        return createSymmetry("Blowfish", new SecretKeySpec(key.getBytes(), "Blowfish"));
    }

    public static IEncryption Camellia(String key) {
//        Security.addProvider(new BouncyCastleProvider());
//        16/24/32
        int len = key.length();
        if (len != 16 || len != 24 || len != 32) {
            if (len < 16)
                key = getkey(key, 16);
            else if (len < 24)
                key = getkey(key, 24);
            else if (len < 32)
                key = getkey(key, 32);
            else
                key = key.substring(0, 32);
        }
        return createSymmetry("Camellia", new SecretKeySpec(key.getBytes(), "Camellia"));
    }

    public static IEncryption IDEA(String key) {
//        Security.addProvider(new BouncyCastleProvider());
        return createSymmetry(IDEA_CBC, new SecretKeySpec(key.getBytes(), IDEA));
    }

    public static IEncryption SEED(String key) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator kgen = KeyGenerator.getInstance("SEED");
        kgen.init(128, new SecureRandom(key.getBytes()));
        return createSymmetry("SEED", new SecretKeySpec(kgen.generateKey().getEncoded(), "SEED"));
    }

    public static IEncryption ChaCha(String key) {
//        Security.addProvider(new BouncyCastleProvider());
        if (key.length() < 16)
            key = getkey(key, 16);
        return createSymmetry("ChaCha", new SecretKeySpec(key.getBytes(), "ChaCha"));
    }

    public static IEncryption Rijndael(String key) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator kgen = KeyGenerator.getInstance("Rijndael");
        kgen.init(128, new SecureRandom(key.getBytes()));
        return createSymmetry("Rijndael", new SecretKeySpec(kgen.generateKey().getEncoded(), "Rijndael"));
    }

    public static IEncryption ARC4(String key) throws Exception {
//        Security.addProvider(new BouncyCastleProvider());
        KeyGenerator kgen = KeyGenerator.getInstance("ARC4");
        kgen.init(128, new SecureRandom(key.getBytes()));
        return createSymmetry("ARC4", new SecretKeySpec(kgen.generateKey().getEncoded(), "ARC4"));
    }

    private static String getkey(String key, int len) {
        if (key.length() >= len)
            return key;
        StringBuffer sb = new StringBuffer(key);
        for (int i = key.length(); i < len; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    private static IEncryption createAsymmetric(String transformation, Key encryptionKey, Key decryptionKey) {
        return (IEncryption) Proxy.newProxyInstance(EncryptionFactory.class.getClassLoader(), new Class[]{IEncryption.class}, new InvocationHandler() {
            IEncryption encryption = new AEncrytion() {
                @Override
                protected Key getKey(boolean encryption) {
                    return encryption ? encryptionKey : decryptionKey;
                }

                @Override
                protected String getTransformation() {
                    return transformation;
                }
            };

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(encryption, args);
            }
        });
    }

    public static IEncryption RSAPrivate(String publicKey, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, 0));
        return createAsymmetric(RSA, keyFactory.generatePrivate(pkcs8KeySpec), keyFactory.generatePublic(x509KeySpec));
    }

    public static IEncryption RSAPublic(String publicKey, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0));
        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, 0));
        return createAsymmetric(RSA, keyFactory.generatePublic(x509KeySpec), keyFactory.generatePrivate(pkcs8KeySpec));
    }

    public static IEncryption DSAPrivate(String publicKey, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0));
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, 0));
        return createAsymmetric("DSA", keyFactory.generatePrivate(pkcs8KeySpec), keyFactory.generatePublic(x509KeySpec));
    }

    public static IEncryption DSAPublic(String publicKey, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0));
        KeyFactory keyFactory = KeyFactory.getInstance("DSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, 0));
        return createAsymmetric("DSA", keyFactory.generatePublic(x509KeySpec), keyFactory.generatePrivate(pkcs8KeySpec));
    }

    public static IEncryption ECCPrivate(String publicKey, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0));
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "SunEC");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, 0));
        return createAsymmetric("ECC", keyFactory.generatePrivate(pkcs8KeySpec), keyFactory.generatePublic(x509KeySpec));
    }

    public static IEncryption ECCPublic(String publicKey, String privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey, 0));
        KeyFactory keyFactory =KeyFactory.getInstance("EC", "SunEC");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.decode(publicKey, 0));
        return createAsymmetric("ECC", keyFactory.generatePublic(x509KeySpec), keyFactory.generatePrivate(pkcs8KeySpec));
    }
}
