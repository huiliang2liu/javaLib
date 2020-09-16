package com.xiaohei.java.lib.http.down;


import java.io.File;


/**
 * com.http.down
 * 2018/11/1 17:45
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
class DownFileImpl implements DownFile {

    {
        int threadNum = Runtime.getRuntime().availableProcessors() * 3;
        threadNum = threadNum << 1;
    }

    DownFileImpl() {

    }


    @Override
    public Down down(String url, int threads, File file) {
        return down(url, threads, file, null);
    }

    @Override
    public Down down(String url, int threads, File file, DownListener listener) {
        return new DownImpl(url, threads, file.getParentFile(), file, listener);
    }

    @Override
    public Down down(String url, File file) {
        return down(url, file, null);
    }

    @Override
    public Down down(String url, File file, DownListener listener) {
        return down(url, 3, file, listener);
    }
}
