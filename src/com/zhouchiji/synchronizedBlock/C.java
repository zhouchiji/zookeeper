package com.zhouchiji.synchronizedBlock;

public class C extends Thread {

    public Common common = new Common();

    @Override
    public void run() {

        Common.A2();
    }
}
