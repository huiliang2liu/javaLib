package com.xiaohei.java.lib.http.down;


import com.xiaohei.java.lib.http.request.Request;
import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.task.InputStreamTask;
import com.xiaohei.java.lib.io.StreamUtil;
import com.xiaohei.java.lib.util.JavaLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

class DownRunnable implements Runnable {
    private final static String TAG = "DownRunnable";
    private final static String START = "start";
    private final static String LEN = "len";
    private final static String END = "end";
    private boolean okhttp = false;
    private int mThreads;
    private File savePath;
    private String mUrl;
    private List<DownEntity> downEntities;
    private boolean mPause = false;
    private File mFile;
    private File temporaryFile;
    private DownListener mDownListener;
    private Object pauseLock = new Object();

    public void pause(boolean pause) {
        mPause = pause;
    }

    public DownRunnable(String url, int threads, File parent, File file, DownListener listener) {
        if (!parent.exists())
            parent.mkdirs();
        savePath = new File(parent, url.hashCode() + ".xml");
        JavaLog.e(TAG, savePath.getAbsolutePath());
        parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        temporaryFile = new File(parent, url.hashCode() + "");
        mUrl = url;
        mThreads = threads;
        mFile = file;
        mDownListener = listener;
    }

    @Override
    public void run() {
        downEntities = url2entities(mUrl, mThreads);
        if (downEntities.size() <= 0) {
            JavaLog.d(TAG, "下载出问题了");
            if (mDownListener != null)
                mDownListener.downFailure(mUrl, mFile);
            temporaryFile.delete();
        }
        for (DownEntity entity : downEntities) {
            Listener listener = new Listener() {
                @Override
                public void success(Response is, DownEntity entity1) {
                    if (mPause) {
                        entity1.pause = true;
                        save();
                        return;
                    }
                    if (is.getCode() < 400)
                        saveFile(is.getInputStream(), entity1);
                    else
                        mDownListener.downFailure(mUrl, mFile);
                }
            };
            if (entity.end == entity.start)
                continue;
            new HttpDown(entity, listener, mUrl);
        }
    }

    private void saveFile(InputStream is, DownEntity entity) {
        try {
            RandomAccessFile raf;
            synchronized (temporaryFile) {
                raf = new RandomAccessFile(temporaryFile, "rwd");
            }
            raf.seek(entity.start);
            int len = -1;
            byte[] buff = new byte[1024 * 1024];
            while ((len = is.read(buff)) > 0) {
                raf.write(buff, 0, len);
                entity.len += len;
                entity.start += len;
                if (mPause) {
                    synchronized (pauseLock) {
                        entity.pause = true;
                        save();
                    }
                    break;
                }
            }
            if (!mPause) {
                entity.start = entity.end;
                end();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mPause = true;
            synchronized (pauseLock) {
                entity.pause = true;
                save();
            }
        }
    }

    public boolean isPause() {
        boolean pause = true;
        for (DownEntity entity : downEntities) {
            pause &= entity.pause;
            if (!pause)
                return pause;
        }
        return pause;
    }

    public boolean isEnd() {
        boolean end = true;
        for (DownEntity entity : downEntities) {
            end &= entity.start == entity.end;
            if (!end)
                return end;
        }
        return end;
    }

    public boolean down() {
        return downEntities != null && downEntities.size() > 0;
    }

    public synchronized float progress() {
        long fileLen = 0;
        long len = 0;
        for (DownEntity entity : downEntities) {
            len += entity.len;
            if (fileLen < entity.end)
                fileLen = entity.end;
        }
        float progress = len * 100.0f / fileLen;
        return new BigDecimal(progress).setScale(2, BigDecimal.ROUND_DOWN).floatValue();
    }

    private synchronized void end() {
        if (!isEnd())
            return;
        JavaLog.e(TAG, "下载完成");
        temporaryFile.renameTo(mFile);
        if (savePath.exists())
            savePath.delete();
        if (mDownListener != null)
            mDownListener.downed(mUrl, mFile);
    }

    private void save() {
        boolean save = true;
        for (DownEntity entity : downEntities) {
            save &= entity.pause;
            if (!save)
                return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < downEntities.size() - 1; i++) {
            DownEntity entity = downEntities.get(i);
            sb.append(entity.start).append(",").append(entity.end).append(",").append(entity.len).append(";");
        }
        DownEntity entity = downEntities.get(downEntities.size() - 1);
        sb.append(entity.start).append(",").append(entity.end).append(",").append(entity.len);
        String json = sb.toString();
        JavaLog.e(TAG, "保存数据" + json);
        if (savePath.exists())
            savePath.delete();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(savePath);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<DownEntity> url2entities(String url, int threads) {
        if (downEntities != null && downEntities.size() > 0)
            return downEntities;
        List<DownEntity> de = new ArrayList<>();
        try {
            if (savePath.exists()) {
                String s = null;
                InputStream is = new FileInputStream(savePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buff = new byte[1024 * 1024];
                byte[] arr = null;
                try {
                    int len = is.read(buff);
                    while (len > 0) {
                        baos.write(buff, 0, len);
                        len = is.read(buff);
                    }
                    arr = baos.toByteArray();
                    s = new String(arr);
                    baos.flush();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    baos.close();
                    is.close();
                }
                if (s != null && !s.isEmpty()) {
                    String[] split = s.split(";");
                    if (split.length > 0) {
                        for (int i = 0; i < split.length; i++) {
                            String jo = split[i];
                            DownEntity entity = new DownEntity();
                            String[] joSplit = jo.split(",");
                            entity.start = Long.valueOf(joSplit[0]);
                            entity.end = Long.valueOf(joSplit[1]);
                            entity.len = Long.valueOf(joSplit[2]);
                            de.add(entity);
                        }
                        return de;
                    }
                }
            }
            Request request = new Request();
            request.setPath(url);
            request.setGetHead(true);
            InputStreamTask inputStreamTask = new InputStreamTask();
            inputStreamTask.setRequest(request);
            Response response = inputStreamTask.connection();
            if (response.getCode() >= 400) {
                return null;
            }
            long fileLen = response.getLen();
            long l = fileLen / threads;
            for (int i = 0; i < threads; i++) {
                DownEntity entity = new DownEntity();
                entity.start = l * i;
                if (threads - 1 == i) {
                    entity.end = fileLen;
                } else {
                    entity.end = entity.start + l;
                }
                de.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return de;
    }
}
