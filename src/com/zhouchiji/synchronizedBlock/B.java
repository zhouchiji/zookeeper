package com.zhouchiji.synchronizedBlock;

public class B extends Thread {

    public Common common = new Common();
    @Override
    public void run() {
        try {
            common.B();
            common.B2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
