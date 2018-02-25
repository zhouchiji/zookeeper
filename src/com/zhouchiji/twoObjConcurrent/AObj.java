package com.zhouchiji.twoObjConcurrent;

public class AObj {


    public synchronized void A(BObj bObj) throws InterruptedException {
        Thread.sleep(1000);
        bObj.B1();
    }

    public void A1() {
        synchronized (this) {
            System.out.println("this is A1 Method");
        }
    }
}
