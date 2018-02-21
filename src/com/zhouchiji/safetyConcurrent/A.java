package com.zhouchiji.safetyConcurrent;

public class A extends Thread {
    @Override
    public void run() {
        try {
            SafeUtils.transfer(123, 234);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
