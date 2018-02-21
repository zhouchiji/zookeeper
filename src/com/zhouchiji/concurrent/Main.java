package com.zhouchiji.concurrent;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        /*AThread a = new AThread();
        a.start();
        BThread b = new BThread();
        b.start();
        Thread.sleep(1000);
        System.out.println(Global.getTransfer().size());
*/
        //线程run方法，是在主线程中运行，所有ThreadLocal的size是2
        Global.setTransfer("key1", "key-main");
        new GlobalThread().run();
        System.out.println(Global.getTransfer().get("key1"));
        System.out.println(Global.getTransfer().get("key2"));


    }
}
