package com.zhouchiji.twoObjConcurrent;

public class BObj {


    public synchronized void B(AObj aObj) throws InterruptedException {
        Thread.sleep(1000);
        aObj.A1();
    }

    public void B1() {
        synchronized (this) {
            System.out.println("this is B1 Method");
        }
    }
}
