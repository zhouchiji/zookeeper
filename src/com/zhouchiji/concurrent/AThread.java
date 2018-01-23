package com.zhouchiji.concurrent;

import java.util.HashMap;
import java.util.Map;

public class AThread extends Thread{

    public static ThreadLocal aThreadLocal = new ThreadLocal();

    @Override
    public void run() {
        Map<String, String> data = new HashMap<>();
        data.put("a","this is a thread");
        aThreadLocal.set(data);
        doSomethings();
        System.out.println(aThreadLocal.get());
    }

    private void doSomethings() {
        Map<String, String> data = (Map<String, String>) aThreadLocal.get();
        data.put("a", "a is change");
    }


}
