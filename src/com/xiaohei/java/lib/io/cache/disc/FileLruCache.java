package com.xiaohei.java.lib.io.cache.disc;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileLruCache {
    public LinkedHashMap<String, File> cacheMap = new LinkedHashMap(0, .75f, true);
    private long cacheSize;
    private long size = 0;

    public FileLruCache(File dir, long cacheSize) {
        if (dir == null)
            throw new NullPointerException("dir id null");
        if (cacheSize <= 0)
            throw new RuntimeException("cacheSize <= 0");
        this.cacheSize = cacheSize;
        if (dir.exists()) {
            File files[] = dir.listFiles();
            if (files == null || files.length <= 0)
                return;
            Arrays.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    long dT = o1.lastModified() - o2.lastModified();
                    if (dT > 0)
                        return 1;
                    if (dT < 0)
                        return -1;
                    return 0;
                }
            });
            for (File file : files)
                add(file);
        } else
            dir.mkdirs();
    }

    public synchronized void add(File file) {
        if (file == null)
            return;
        size += file.length();
        cacheMap.put(file.getName(), file);
        while (size > cacheSize) {
            Map.Entry<String, File> toEvict = cacheMap.entrySet().iterator().next();
            remove(toEvict.getKey());
        }
    }

    public synchronized boolean remove(String key) {
        File file = cacheMap.remove(key);
        if (file == null)
            return false;
        size -= file.length();
        file.delete();
        return true;
    }

    public synchronized File get(String key) {
        File file = cacheMap.get(key);
        if (file == null)
            return file;
        file.setLastModified(System.currentTimeMillis());
        return file;
    }

}
