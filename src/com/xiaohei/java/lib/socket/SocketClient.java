package com.xiaohei.java.lib.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SocketClient {
    private Socket socket;
    protected OutputStream outputStream;
    protected InputStream inputStream;
    protected Proxy proxy;
    protected int connectTimeout;
    protected int readTimeout;
    protected String hostName;
    protected int port;
    protected InetAddress address;

    SocketClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        System.out.println(String.format("hostName:%s,port:%d", hostName, port));
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public static String parsHost(String url) {
        Matcher matcher = Pattern.compile(".+://([^:]+):.*/").matcher(url);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    public static int parsPort(String url) {
        Matcher matcher = Pattern.compile(".+://[^:]+:([^/]+)/").matcher(url);
        if (matcher.find())
            return Integer.valueOf(matcher.group(1));
        if (url.startsWith("http:"))
            return 80;
        if (url.startsWith("https:"))
            return 443;
        return 21;
    }


    protected InetAddress[] parsAddress(String host) throws UnknownHostException {
        return InetAddress.getAllByName(host);
    }

    public final void connect() throws IOException {
        InetAddress[] address = parsAddress(hostName);
        if (address == null || address.length <= 0) {
            throw new RuntimeException("address is null");
        }
        for (InetAddress addres : address) {
            socket = createSocket(addres, port);
            this.address = addres;
            if (socket != null)
                break;
        }

        if (socket == null) {
            throw new RuntimeException("socket is null");
        }
        outputStream = socket.getOutputStream();
        inputStream = socket.getInputStream();
        communication();
    }

    protected abstract void communication() throws IOException;

    public void write(byte[] buf, int off, int len) {
        try {
            outputStream.write(buf, off, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void error() {

    }

    public String readLine() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while (true) {
            int c = read();
            if (c == '\n') {
                break;
            }
            if (c == '\r')
                continue;
            baos.write(c);
        }
        return buff2string(baos.toByteArray());
    }

    private String buff2string(byte[] buff) {
        return new String(buff);
    }

    private int read() {
        while (true) {
            try {
                int c = inputStream.read();
                if (c != -1)
                    return c;
            } catch (IOException e) {
                if (Test.DEBUG)
                    e.printStackTrace();
            }
        }
    }

    public boolean isConnected() {
        if (socket == null)
            return false;
        return socket.isConnected();
    }

    protected abstract Socket createSocket(InetAddress address, int port);

    public void disconnect() throws IOException {
        if (socket == null)
            return;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
