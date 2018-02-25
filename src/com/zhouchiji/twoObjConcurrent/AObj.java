package com.zhouchiji.twoObjConcurrent;

public class AObj {

    private BObj bObj;


    public synchronized void A() throws InterruptedException {
        Thread.sleep(1000);
        bObj = new BObj();
        bObj.B1();
    }

    public void A1() {
        synchronized (this) {
            System.out.println("this is A1 Method");
        }
    }
}
