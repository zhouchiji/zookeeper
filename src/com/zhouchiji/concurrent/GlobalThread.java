package com.zhouchiji.concurrent;

public class GlobalThread implements Runnable{


    @Override
    public void run() {
        Global.setTransfer("key2", "key-GlobalThread");
    }
}
