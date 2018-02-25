package com.zhouchiji.twoObjConcurrent;

public class Run {
    public static void main(String[] args) {
        final AObj a1 = new AObj();
        final BObj b1 = new BObj();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    a1.A();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    b1.B();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        a.start();
        b.start();
        //解释：这里new的a1和b1对象，在我们线程中调用的方法里面又重新new了两个对象，所以是不同的对象，在对象锁上是互斥的，所以可以调用
    }
}
