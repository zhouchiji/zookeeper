package com.zhouchiji.synchronizedBlock;

public class A extends Thread {

    public Common common = new Common();
    @Override
    public void run() {
        try {
            Common.A();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
