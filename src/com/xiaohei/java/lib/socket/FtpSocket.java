package com.xiaohei.java.lib.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FtpSocket extends SocketClient {
    private static final String FORMAT_LOCAL = "ftp:.*//([^:]*):([^@]*)@[^/]*/(.*)";
    String user;
    String password;
    String port;
    String host;
    String file;

    public FtpSocket(Request request) {
        super(getHost(request.path), getPort(request.path));
        Matcher matcher = Pattern.compile(FORMAT_LOCAL).matcher(request.path);
        if (!matcher.find()) {
            throw new RuntimeException("not found user");
        }
        user = matcher.group(1);
        password = matcher.group(2);
        file = matcher.group(3);
        System.out.println(user);
        System.out.println(password);
        System.out.println(file);
    }

    private static String getHost(String url) {
        Matcher matcher = Pattern.compile("ftp:.*//.*@([^/]*)/.*").matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("not found user");
        }
        String test = matcher.group(1);
        int index = test.indexOf(":");
        if (index > 0)
            return test.substring(0, index);
        return test;
    }

    private static int getPort(String url) {
        Matcher matcher = Pattern.compile("ftp:.*//.*@([^/]*)/.*").matcher(url);
        if (!matcher.find()) {
            throw new RuntimeException("not found user");
        }
        String test = matcher.group(1);
        int index = test.indexOf(":");
        if (index > 0)
            return Integer.valueOf(test.substring(index + 1));
        return 21;
    }

    @Override
    protected void communication() throws IOException {
        String line = readLine();
        System.out.println(line);
        if (!line.startsWith("220")) {
            throw new IOException("unknow response after connect!");
        }
        sendLine("USER " + user);
        line = readLine();
        System.out.println(line);
        if (!line.startsWith("331")) {
            throw new IOException("unknow response after send user");
        }
        sendLine("PASS " + password);
        line = readLine();
        System.out.println(line);
        if (!line.startsWith("230")) {
            throw new IOException("unknow response after send pass");
        }
        System.out.println(line);
        sendLine("CWD m/z/mzc00200e08eido/g00339ln2ts");
        line = readLine();
        System.out.println(line);
//        for (int i = 0; i < 2; i++) {
//            if (i % 2 == 0)
//                sendLine("PASV");
//            else
//                sendLine("EPSV");
//            line = readLine();
//            System.out.println(line);
//            String ip = null;
//            int port1 = -1;
//            int opening = line.indexOf('(');
//            int closing = line.indexOf(')', opening + 1);
//
//            if (closing > 0) {
//                String dataLink = line.substring(opening + 1, closing);
//                StringTokenizer tokenizer = new StringTokenizer(dataLink, ",");
//                try {
//                    ip = tokenizer.nextToken() + "." + tokenizer.nextToken() + "."
//                            + tokenizer.nextToken() + "." + tokenizer.nextToken();
//                    port1 = Integer.parseInt(tokenizer.nextToken()) * 256
//                            + Integer.parseInt(tokenizer.nextToken());
//                } catch (Exception e) {
//                    throw new IOException("SimpleFTP received bad data link information: "
//                            + line);
//                }
//            }
//
//            System.out.println(ip + "  " + port1);
//            sendLine("LIST");
//            Socket dataSocket = new Socket(ip, port1);
//            line = readLine();
//            System.out.println(line);
//        }
//        sendLine("EPSV ALL");
//        line = readLine();
//        System.out.println(line);
//        sendLine("EPSV");
//        line = readLine();
//        String portlist = line.substring(line.indexOf("(|||") + 4, line.indexOf("|)"));
//        System.out.println(portlist);
//        System.out.println(line);
//        sendLine("LIST "+file);
//        new ShowList(Integer.valueOf(portlist)).run();
//        line = readLine();
//        System.out.println(line);
//        showFile("m/z/mzc00200e08eido/k00331hr4pl");
//        System.err.println("========================");
        showFile(file);
//        sendLine("PASV");
//        line = readLine();
//        System.out.println(line);
//        sendLine("CWD m/z/mzc00200e08eido/g00339ln2ts");
//        line = readLine();
//        System.out.println(line);
//        sendLine("PORT");
//        sendLine("RETR "+file);
//        line = readLine();
//        System.out.println(line);

    }

    private void showFile(String file) throws IOException {
        sendLine("EPSV ALL");
        String line = readLine();
        System.out.println(line);
        sendLine("EPSV");
//        String line = "";
        line = readLine();
        System.out.println(line);
        String portlist = line.substring(line.indexOf("(|||") + 4, line.indexOf("|)"));
        System.out.println(portlist);
        sendLine("LIST " + file);
        new ShowList(Integer.valueOf(portlist)).run();
//        sendLine("ABOR");
        line = readLine();
        System.out.println(line);
//        sendLine("LIST " + file);
//        line = readLine();
//        System.out.println(line);
    }

    private class ShowList extends Thread {
        public int port;
        InputStream is;

        public ShowList(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            super.run();
            Socket socket = null;
            try {
                socket = createSocket(address, port);
                if (socket == null)
                    return;
                is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                String line;
                int i = 0;
                while (true) {
                    line = readLine();
                    System.out.println(line + "========");
                    if (line == null || line.isEmpty()) {
                        if (i == 1)
                            break;
                        i = 1;
                        os.write("CWD m/z/mzc00200e08eido/g00339ln2ts\r\n".getBytes());
                        continue;
                    }
                    System.out.println(line);
                }
                //关闭链接
//                is.close();
//                os.close();
//                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private int read() {
            while (true) {
                try {
                    int c = is.read();
                    if (c != -1)
                        return c;
                    else
                        break;
                } catch (IOException e) {
                    if (Test.DEBUG)
                        e.printStackTrace();
                }
            }
            return -1;
        }

        public String readLine() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (true) {
                int c = read();
                if (c == -1)
                    break;
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
    }

    private void sendLine(String test) throws IOException {
        System.out.println(test);
        outputStream.write((test + "\r\n").getBytes());
    }

    @Override
    protected Socket createSocket(InetAddress a, int port) {
        try {
            SocketAddress address = new InetSocketAddress(a, port);
            Socket socket = null;
            if (proxy != null) {
                socket = new Socket(proxy);
            } else
                socket = new Socket();
            socket.connect(address, connectTimeout);
            socket.setSoTimeout(readTimeout);
            return socket;
        } catch (IOException e) {
            if (Test.DEBUG)
                e.printStackTrace();
        }
        return null;
    }

    @Override
    public void disconnect() throws IOException {
        sendLine("QUIT");
        super.disconnect();
    }
}
