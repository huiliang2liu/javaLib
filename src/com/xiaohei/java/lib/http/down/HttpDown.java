package com.xiaohei.java.lib.http.down;


import com.xiaohei.java.lib.http.HttpManage;
import com.xiaohei.java.lib.http.request.DownRequest;
import com.xiaohei.java.lib.http.response.Response;
import com.xiaohei.java.lib.http.task.InputStreamTask;
import com.xiaohei.java.lib.thread.PoolManager;


class HttpDown implements Runnable {
    private static final String TAG = "HttpDown";
    private DownEntity mEntity;
    private Listener mListener;
    private String mUrl;

    public HttpDown(DownEntity entity, Listener listener, String url) {
        mEntity = entity;
        mListener = listener;
        mUrl = url;
        PoolManager.io(this);
    }

    @Override
    public void run() {
        DownRequest downRequest = new DownRequest();
        downRequest.setPath(mUrl);
        downRequest.setStartAndEnd(mEntity.start, mEntity.end);
        InputStreamTask task = new InputStreamTask();
        task.setRequest(downRequest);
        Response ris = task.connection();
        mListener.success(ris, mEntity);
    }
}
