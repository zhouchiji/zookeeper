package com.zhouchiji.concurrent;

import java.util.HashMap;
import java.util.Map;

public class BThread extends Thread{

    public static ThreadLocal bThreadLocal = new ThreadLocal();

    @Override
    public void run() {
        super.run();
        Map<String, String> data = new HashMap<>();
        data.put("b","this is b thread");
        bThreadLocal.set(data);
        doSomethings();
        System.out.println(bThreadLocal.get());
    }

    private void doSomethings() {
        Map<String, String> data = (Map<String, String>) bThreadLocal.get();
        data.put("b", "b is change");
    }
}
