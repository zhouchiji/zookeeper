package com.zhouchiji.concurrent;

import java.util.HashMap;
import java.util.Map;

public class BThread extends Thread{

    public static ThreadLocal bThreadLocal = new ThreadLocal();

    @Override
    public void run() {
        super.run();
        Map<String, Object> data = Global.getTransfer();
//        data.put("b","this is b thread");
        Global.setTransfer("b", "this is b thread");
//        doSomethings();
        System.out.println(Global.getTransfer().get("b"));
    }

    private void doSomethings() {
        Map<String, Object> data = Global.getTransfer();
        data.put("b", "b is change");
    }
}
