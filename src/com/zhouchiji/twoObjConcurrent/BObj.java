package com.zhouchiji.twoObjConcurrent;

public class BObj {


    AObj aObj;

    public synchronized void B() throws InterruptedException {
        Thread.sleep(1000);
        aObj = new AObj();
        aObj.A1();
    }

    public void B1() {
        synchronized (this) {
            System.out.println("this is B1 Method");
        }
    }
}
