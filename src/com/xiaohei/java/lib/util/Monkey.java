package com.xiaohei.java.lib.util;

import com.xiaohei.java.lib.io.StreamUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Monkey {

    List<Action> actions=new ArrayList<>();
    List<String> packages=new ArrayList<>();
    private long throttle=-1;//延迟时间
    private long times=1000;
    private String savaPath;

    public  Monkey(){

    }

    public void exe(){
        String cmd=toString();
        try {
            Process process=Runtime.getRuntime().exec(cmd);
            if(savaPath==null||savaPath.isEmpty())
                System.out.println(""+ StreamUtil.stream2string(process.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        StringBuffer sb=new StringBuffer("adb shell monkey");
        for (String pa:packages)
            sb.append(" -p ").append(pa);
        if(throttle>0)
            sb.append(" --throttle ").append(throttle);
        for (Action action:actions)
            sb.append(action.toString());
        sb.append(" -v -v -v ");
        if(times<=0)
            times=1000;
        sb.append(times);
        if(savaPath!=null&&!savaPath.isEmpty())
            sb.append(" >").append(savaPath);
        return sb.toString();
    }

    public Monkey setSavaPath(String savaPath) {
        this.savaPath = savaPath;
        return this;
    }

    public Monkey setTimes(long times) {
        this.times = times;
        return this;
    }

    public Monkey setThrottle(long throttle){
        this.throttle=throttle;
        return this;
    }

    public Monkey addPackage(String name){
        packages.add(name);
        return this;
    }
    public Monkey addAction(String name,int percent){
        if(percent<0|percent>100)
            percent=100;
        actions.add(new Action(name,percent));
        return this;
    }
    private static  class Action{
        Action(String name,int percent){
            this.name=name;
            this.percent=percent;
        }
        String name;//--pct-touch 指定touch事件的百分比percent  --pct-motion 指定motion事件的百分比percent --pct-trackball 指定轨迹球事件百分比percent  --pct-nav  指定基本导航事件百分比percent --pct-majornav :设定主要导航事件百分比percent，兼容中间键，返回键，菜单按键 --pct-syskeys 设定系统事件百分比percent，比如HOME,BACK,拨号及音量调节等事件 --pct-appswitch :设定启动activity的事件百分比percent --pct-anyevent 设定不常用事件地百分比
        int percent;

        @Override
        public String toString() {
            return String.format(" %s %d",name,percent);
        }
    }
}
