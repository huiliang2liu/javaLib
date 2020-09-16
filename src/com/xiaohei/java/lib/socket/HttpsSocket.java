package com.xiaohei.java.lib.socket;

import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpsSocket extends HttpSocket {
    public HttpsSocket(Request request) {
        super(request);
    }

    @Override
    protected Socket createSocket(InetAddress a, int port) {
        SocketFactory factory = createSSLSocketFactory();
        if (factory == null) {
            System.out.println("SSLSocketFactory is null");
            return null;
        }
        try {
            return factory.createSocket(a, port);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private X509TrustManager createManager() {
        return new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                for (X509Certificate certificate : chain) {
                    certificate.checkValidity();
                }
            }

        };
    }

    private SSLSocketFactory test() {
        X509TrustManager xtm = createManager();
        TrustManager tm[] = {xtm};
        try {
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("NewSunX509");
            SSLContext ctx = SSLContext.getInstance("SSL");
            ctx.init(null, tm, null);
            return ctx.getSocketFactory();
        } catch (Exception e) {
            if (Test.DEBUG)
                e.printStackTrace();
            return null;
        }
    }

    private SocketFactory createSSLSocketFactory() {
        return SSLSocketFactory.getDefault();
    }
}
