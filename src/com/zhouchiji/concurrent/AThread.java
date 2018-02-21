package com.zhouchiji.concurrent;

import java.util.HashMap;
import java.util.Map;

public class AThread extends Thread{

    public static ThreadLocal aThreadLocal = new ThreadLocal();

    @Override
    public void run() {
        Map<String, Object> data = Global.getTransfer();
//        data.put("a","this is a thread");
//        aThreadLocal.set(data);
        Global.setTransfer("a", "this is a Thread");
//        doSomethings();
        System.out.println(Global.getTransfer().get("a"));
    }

    private void doSomethings() {
        Map<String, Object> data = Global.getTransfer();
        data.put("a", "a is change");
    }


}
