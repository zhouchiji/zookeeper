package com.zhouchiji.twoObjConcurrent;

public class Run {
    public static void main(String[] args) {
        final AObj a1 = new AObj();
        final BObj b1 = new BObj();
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    a1.A(b1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    b1.B(a1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        a.start();
        b.start();
    }
}
