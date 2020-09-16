package com.xiaohei.java.lib.socket;

import javax.net.ssl.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SocketWeb {
    private Request request;
    private int port = 10;
    private String resourcePath;

    public SocketWeb(Request request) {
        this.request = request;
    }

    public Response connect() throws UnknownHostException, IOException {
        String url = request.path;
        port = getPort(url);
        String host = getHost(url);
        boolean hasPort = false;
        if (host.contains(":")) {
            String[] s = host.split(":");
            port = Integer.valueOf(s[1]);
            host = s[0];
            hasPort = true;
        }
        resourcePath = getResourcePath(url);
        InetAddress[] addresses = null;
        addresses = pars(host);
        if (addresses == null || addresses.length <= 0)
            throw new RuntimeException("you address is empty");
        Socket socket = null;
        InetAddress address = null;
        boolean https = url.startsWith("https");
        for (InetAddress addr : addresses) {
            socket = connect(addr, port, https);
            address = addr;
            if (socket != null)
                break;
        }
        Response response = new Response();
        if (socket == null) {
            response.code = 400;
            response.error = "not found service";
        } else {
//            response.outputStream = socket.getOutputStream();
//            write(response.outputStream, hasPort ? String.format("%s:%s", address.getHostName(), port) : address.getHostName());
            read(socket.getInputStream(), response);
        }

        return response;
    }

    private static final String FIRST_FORMAT = "%s /%s HTTP/1.1\r\n";
    private static final String HEARDER_FORMAT = "%s: %s\r\n";

    private void write(OutputStream os, String host) throws IOException {
        os.write(hearder2byte(String.format(FIRST_FORMAT, request.method, resourcePath)));
        os.write(hearder2byte(String.format(HEARDER_FORMAT, "HOST", host)));
        Header header = request.header;
        if (header != null) {
            Map<String, List<String>> headerMap = header.headerMap;
            for (String key : headerMap.keySet()) {
                List<String> values = headerMap.get(key);
                if (values == null || values.size() <= 0)
                    continue;
                for (String value : values) {
                    os.write(hearder2byte(String.format(HEARDER_FORMAT, key, value)));
                }
            }
        }
        os.write(hearder2byte("\r\n"));
    }

    private byte[] hearder2byte(String line) {
        System.err.println(line);
        return line.getBytes();
    }

    private String buff2string(byte[] buff) {
        return new String(buff);
    }

    private int len = 0;
    private int code;

    private void read(InputStream is, Response response) {
        Header header = readHeader(is);
        response.responseHeader = header;
        response.code = code;
        if (code == 200)
            response.inputStream = new MyInputStream(is, len);
    }

    private Header readHeader(InputStream is) {
        String line;
        Header header = new Header();
        while (true) {
            line = readLine(is);
            if (line == null || line.isEmpty())
                break;
            String[] sl = line.split(":");
            String key;
            String value;
            if (sl.length > 1) {
                key = sl[0];
                value = sl[1].replaceFirst(" ", "");
                if (key.equals("Content-Length")) {
                    try {
                        len = Integer.valueOf(value);
                        System.out.println(len);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                key = "null";
                value = line;
                if (line.startsWith("HTTP/1.")) {
                    String[] cs = line.split(" ");
                    try {
                        code = Integer.valueOf(cs[1]);
                    } catch (Exception e) {
                        if (Test.DEBUG)
                            e.printStackTrace();
                    }
                }
            }
            header.addHeader(key, value);
        }
        return header;
    }

    private String readLine(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int c = read(is);
            if (c == '\r') {
                int d = read(is);
                if (d == '\n') {
                    break;
                } else {
                    baos.write(c);
                }
            } else {
                baos.write(c);
            }
        }
        return buff2string(baos.toByteArray());
    }

    private int read(InputStream is) {
        while (true) {
            try {
                int c = is.read();
                if (c != -1)
                    return c;
            } catch (IOException e) {
                if (Test.DEBUG)
                    e.printStackTrace();
            }
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
                for (X509Certificate certificate : chain)
                {
                    certificate.checkValidity();
                }
            }

        };
    }

    private SSLSocketFactory createSSLSocketFactory() {
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

    Socket connect(InetAddress a, int port, boolean https) {
        if (https) {
            SSLSocketFactory factory = createSSLSocketFactory();
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
        try {
            SocketAddress address = new InetSocketAddress(a, port);
            Socket socket = new Socket();
            socket.connect(address, request.connectTimeout);
            socket.setSoTimeout(request.readTimeout);
            return socket;
        } catch (IOException e) {
            if (Test.DEBUG)
                e.printStackTrace();
        }
        return null;
    }

    private String getHost(String url) {
        Matcher matcher = Pattern.compile(".+://([^/]+)/").matcher(url);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    private int getPort(String url) {
        if (url.startsWith("http:"))
            return 80;
        return 443;
    }

    private String getResourcePath(String url) {
        Matcher matcher = Pattern.compile(".+://.*/(.*)").matcher(url);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    private InetAddress[] pars(String host) throws UnknownHostException {
        return InetAddress.getAllByName(host);
    }

    private static class MyInputStream extends InputStream {
        private InputStream is;
        private int len;

        MyInputStream(InputStream is, int len) {
            this.is = is;
            this.len = len;
        }

        @Override
        public int read() throws IOException {
            if (len <= 0)
                return -1;
            int re = is.read();
            if (re != -1)
                len -= 1;
            return re;
        }

        @Override
        public void close() throws IOException {
            is.close();
        }
    }
}
