package com.zhouchiji.synchronizedBlock;

public class Common {

    public synchronized static void A() throws InterruptedException {
        Thread.sleep(5000);
        System.out.println("this is synchronized method");
        Thread.sleep(5000);
    }

    public void B() throws InterruptedException {
        synchronized (this) {
            System.out.println("this is synchronized object");
            Thread.sleep(3000);
        }
    }

    public synchronized static void A2() {
        System.out.println("this is A2 synchronized method");
    }

    public void B2() {
        synchronized (this) {
            System.out.println("this is B2 synchronized object");
        }
    }
}
