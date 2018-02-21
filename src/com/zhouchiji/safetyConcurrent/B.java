package com.zhouchiji.safetyConcurrent;

public class B extends Thread {
    @Override
    public void run() {
        try {
            SafeUtils.transfer(234, 123);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
