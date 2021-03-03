package com.xiaohei.java.lib.io.cache.disc;

import java.io.*;
import java.security.MessageDigest;

public class LruDiskCache {
    public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
    public static final int DEFAULT_COMPRESS_QUALITY = 100;

    private static final String ERROR_ARG_NULL = " argument must be not null";
    private static final String ERROR_ARG_NEGATIVE = " argument must be positive number";
    protected DiskLruCache cache;
    protected int bufferSize = DEFAULT_BUFFER_SIZE;
    protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

    public LruDiskCache(File cacheDir, long cacheMaxSize) throws IOException {
        this(cacheDir, cacheMaxSize, 0);
    }


    public LruDiskCache(File cacheDir, long cacheMaxSize, int cacheMaxFileCount) throws IOException {
        if (cacheDir == null) {
            throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
        }
        if (cacheMaxSize < 0) {
            throw new IllegalArgumentException("cacheMaxSize" + ERROR_ARG_NEGATIVE);
        }
        if (cacheMaxFileCount < 0) {
            throw new IllegalArgumentException("cacheMaxFileCount" + ERROR_ARG_NEGATIVE);
        }

        if (cacheMaxSize == 0) {
            cacheMaxSize = Long.MAX_VALUE;
        }
        if (cacheMaxFileCount == 0) {
            cacheMaxFileCount = Integer.MAX_VALUE;
        }
        initCache(cacheDir, cacheMaxSize, cacheMaxFileCount);
    }

    private void initCache(File cacheDir, long cacheMaxSize, int cacheMaxFileCount)
            throws IOException {
        try {
            cache = DiskLruCache.open(cacheDir, 1, 1, cacheMaxSize, cacheMaxFileCount);
        } catch (IOException e) {
            throw e;
        }
    }


    public File getDirectory() {
        return cache.getDirectory();
    }


    public File get(String url) {
        DiskLruCache.Snapshot snapshot = null;
        try {
            snapshot = cache.get(getKey(url));
            return snapshot == null ? null : snapshot.getFile(0);
        } catch (IOException e) {
            return null;
        } finally {
            if (snapshot != null) {
                snapshot.close();
            }
        }
    }

    public boolean save(String url, InputStream is) throws IOException {
        DiskLruCache.Editor editor = cache.edit(getKey(url));
        if (editor == null) {
            return false;
        }

        OutputStream os = new BufferedOutputStream(editor.newOutputStream(0), bufferSize);
        boolean copied = false;
        try {
            byte[] buf = new byte[bufferSize];
            int len = 0;
            while ((len = is.read(buf, 0, bufferSize)) != -1)
                os.write(buf, 0, len);
            copied = true;
        } finally {
            os.close();
            if (copied) {
                editor.commit();
            } else {
                editor.abort();
            }
        }
        return copied;
    }


    public boolean remove(String url) {
        try {
            return cache.remove(getKey(url));
        } catch (IOException e) {
            return false;
        }
    }


    public void close() {
        try {
            cache.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cache = null;
    }


    public void clear() {
        try {
            cache.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            initCache(cache.getDirectory(), cache.getMaxSize(), cache.getMaxFileCount());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final char[] HEX_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};

    private String getKey(String url) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            byte[] bytes = md5.digest();
            int len = bytes.length;
            char[] hexChars = new char[len << 1];
            for (int i = 0; i < len; i++) {
                int index = i << 1;
                int v = bytes[i] & 0xff;
                hexChars[index] = HEX_ARRAY[v >>> 4];
                hexChars[index + 1] = HEX_ARRAY[v & 0xf];
            }
            return new String(hexChars);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }


    public void setCompressQuality(int compressQuality) {
        this.compressQuality = compressQuality;
    }
}
