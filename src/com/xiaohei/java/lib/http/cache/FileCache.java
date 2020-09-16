package com.xiaohei.java.lib.http.cache;

import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.io.FileUtil;
import com.xiaohei.java.lib.util.JavaLog;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileCache implements Cache {
    private static final String TAG = "FileCache";
    private File cache;
    private MessageDigest md5;
    private long cachSize;

    public FileCache(File cache, long cacheSize) {
        this.cache = cache;
        if (!cache.exists())
            cache.mkdirs();
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.cachSize = cacheSize;
    }

    @Override
    public Response getCache(String url) {
        File head = new File(cache, value(url, true));
        File content = new File(cache, value(url, false));
        if (!head.exists() || !content.exists()) {
            if (head.exists())
                head.delete();
            if (content.exists())
                content.delete();
            return null;
        }
        head.setLastModified(System.currentTimeMillis());
        Map<String, List<String>> heads = new HashMap<>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(head));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty())
                    continue;
                String split[] = line.split(":", 2);
                if (split.length < 2)
                    continue;
                String values[] = split[1].split(",");
                List<String> list = new ArrayList<>(values.length);
                for (String value : values)
                    list.add(value);
                heads.put(split[0], list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

        if (!content.exists())
            return null;
        content.setLastModified(System.currentTimeMillis());
        InputStream is = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        long length = 0;
        ByteArrayInputStream bais = null;
        try {
            is = new FileInputStream(content);
            byte[] buff = new byte[1024 * 1024];
            int len = 0;
            while ((len = is.read(buff)) > 0) {
                length += len;
                os.write(buff, 0, len);
            }
            bais = new ByteArrayInputStream(os.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Response response = new Response();
        response.setCode(200);
        response.setInputStream(bais);
        response.setResponseHead(heads);
        response.setLen(length);
        return response;
    }

    @Override
    public void cache(Response response, String url) {
        if (cachSize <= 0)
            return;
        File head = new File(cache, value(url, true));
        if (head.exists())
            head.setLastModified(System.currentTimeMillis());
        Map<String, List<String>> heads = response.getResponseHead();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(head));
            for (String key : heads.keySet()) {
                List<String> values = heads.get(key);
                StringBuilder sb = new StringBuilder();
                sb.append(key).append(":");
                for (String s : values)
                    sb.append(s).append(",");
                writer.write(sb.substring(0, sb.length() - 1));
                writer.newLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        File content = new File(cache, value(url, false));
        if (content.exists())
            content.setLastModified(System.currentTimeMillis());
        System.out.println(content.getAbsolutePath());
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(content);
            InputStream is = response.getInputStream();
            if (is == null) {
                os.write(response.getString().getBytes());
            } else {
                boolean mark = is.markSupported();
                System.out.println(String.format("markSupported=%b", mark));
                if (mark) {
                    System.out.println("markSupported");
                    is.mark((int) response.getLen());
                    byte[] buff = new byte[1024 * 1024];
                    int len = 0;
                    while ((len = is.read(buff)) > 0) {
                        os.write(buff, 0, len);
                    }
                    is.reset();
                } else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buff = new byte[1024 * 1024];
                    int len = 0;
                    while ((len = is.read(buff)) > 0) {
                        baos.write(buff, 0, len);
                        os.write(buff, 0, len);
                    }
                    response.setInputStream(new ByteArrayInputStream(baos.toByteArray()));
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null)
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        File[] files = cache.listFiles();
        files = FileUtil.ascendTime(files);
        int len = 0;
        for (File file : files) {
            if (len > cachSize) {
                file.delete();
            } else
                len += file.length();
        }
        JavaLog.d(TAG, String.format("cache size %s", len));
    }

    private String value(String url, boolean head) {
        String value = head ? url + "hear" : url;
        if (md5 != null) {
            md5.update(value.getBytes());
            value = bytes2hex(md5.digest());
        }
        return value;
    }

    private static final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private static String bytes2hex(byte[] bytes) {
        int len = bytes.length;
        char[] hexChars = new char[len << 1];
        for (int i = 0; i < len; i++) {
            int index = i << 1;
            int v = bytes[i] & 0xff;
            hexChars[index] = hexArray[v >>> 4];
            hexChars[index + 1] = hexArray[v & 0xf];
        }
        return new String(hexChars);
    }
}
