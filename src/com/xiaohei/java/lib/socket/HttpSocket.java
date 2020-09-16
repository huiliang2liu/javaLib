package com.xiaohei.java.lib.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpSocket extends SocketClient {
    private static final String[] HAS_OTUPUT = {"POST"};
    private Request request;
    private Header header;
    private int len = 0;
    private int code = 404;
    private Response response;
    protected String resourcePath;

    public HttpSocket(Request request) {
        this(parsHost(request.path), parsPort(request.path));
        setConnectTimeout(request.connectTimeout);
        setReadTimeout(request.readTimeout);
        resourcePath = getResourcePath(request.path);
        this.request = request;
    }

    protected HttpSocket(String hostName, int port) {
        super(hostName, port);
        response = new Response();
        response.code = code;
        response.len = len;
        response.error = "not found service";
        header = new Header();
    }

    @Override
    protected void communication() throws IOException {

        writeHeader();
        if (request.inputStream != null) {
            byte[] buf = new byte[1024 * 1024];
            int len = 0;
            while ((len = request.inputStream.read(buf)) > 0){
                System.err.println("写入数据");
                outputStream.write(buf, 0, len);
            }
            outputStream.write("\r\n".getBytes());
        }
        readHeader(inputStream);
        response.code = code;
        response.len = len;
        response.responseHeader = header;

        response.inputStream = new MyInputStream(inputStream, len);
    }

    public Response getResponse() {
        return response;
    }

    private String getResourcePath(String url) {
        Matcher matcher = Pattern.compile(".+://[^/]*/(.*)").matcher(url);
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    public boolean hasOutputStream() {
        for (String has : HAS_OTUPUT)
            if (has.equals(request.method))
                return true;
        return false;
    }

    private static final String FIRST_FORMAT = "%s /%s HTTP/1.1\r\n";
    private static final String HEARDER_FORMAT = "%s: %s\r\n";

    protected void writeHeader() throws IOException {
        outputStream.write(hearder2byte(String.format(FIRST_FORMAT, request.method, resourcePath)));
        outputStream.write(hearder2byte(String.format(HEARDER_FORMAT, "HOST", hostName)));
        Header header = request.header;
        if (header != null) {
            Map<String, List<String>> headerMap = header.headerMap;
            for (String key : headerMap.keySet()) {
                List<String> values = headerMap.get(key);
                if (values == null || values.size() <= 0)
                    continue;
                for (String value : values) {
                    outputStream.write(hearder2byte(String.format(HEARDER_FORMAT, key, value)));
                }
            }
        }
        outputStream.write(hearder2byte("\r\n"));
    }

    private byte[] hearder2byte(String line) {
        System.err.println(line);
        return line.getBytes();
    }

    private Header readHeader(InputStream is) {
        String line;
        while (true) {
            line = readLine();
//            line = line.replace("\r", "");
            if (line == null || line.isEmpty()) {
                System.err.println("======");
                break;
            }
            String key;
            String value;
            if (line.contains(":")) {
                String[] sl = line.split(":", 2);
                key = sl[0];
                value = sl[1].trim();
                if (key.equals("Content-Length")) {
                    try {
                        len = Integer.valueOf(value.trim());
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
            System.err.println("createSocket");
            return socket;
        } catch (IOException e) {
            System.err.println("createSocket error");
            if (Test.DEBUG)
                e.printStackTrace();
        }
        return null;
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
