package com.xiaohei.java.lib.encryption;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.Security;

public class SignFactroy {
    private static ISign create(String algorithm) {
        if (algorithm == null || algorithm.isEmpty())
            algorithm = "MD5";
        final String algorithm1 = algorithm;
        return (ISign) Proxy.newProxyInstance(SignFactroy.class.getClassLoader(), new Class[]{ISign.class}, new InvocationHandler() {
            ISign sign = new Message(algorithm1);

            {
                if (sign == null)
                    sign = new Message("MD5");
            }

            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(sign, args);
            }
        });
    }

    public static ISign MD5() {
        return create("MD5");
    }

    public static ISign SHA1() {
        return create("SHA-1");
    }

    public static ISign SHA256() {
        return create("SHA-256");
    }

    public static ISign SHA512() {
        return create("SHA-512");
    }

    public static ISign SHA224() {
        return create("SHA-224");
    }

    public static ISign SHA384() {
        return create("SHA-384");
    }

    public static ISign SHA3_256() {
//        Security.addProvider(new BouncyCastleProvider());
        return create("SHA3-256");
    }

    public static ISign RSASign(String publicKey, String privateKey) {
        return new RSASign(publicKey, privateKey);
    }

    public static ISign DSASign(String publicKey, String privateKey) {
        return new DSASign(publicKey, privateKey);
    }
}
