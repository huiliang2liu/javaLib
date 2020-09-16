package com.xiaohei.java.lib.http.down;


class DownEntity {
    public long start;
    public long end;
    public long len=0;
    public boolean pause = false;

    @Override
    public String toString() {
        return String.format("start:%s,end:%s,len:%s,pause:%s",start,end,len,pause);
    }
}
