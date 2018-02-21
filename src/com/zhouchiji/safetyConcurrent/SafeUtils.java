package com.zhouchiji.safetyConcurrent;

public class SafeUtils {
    public static Object o1 = new Object();
    public static Object o2 = new Object();

    public static void transfer(int i1, int i2) throws InterruptedException {
        if (i1 > i2) {
            synchronized (o1) {
                Thread.sleep(100);
                synchronized (o2) {
                    System.out.println("o1 -> o2");
                }
            }
        }
        else if (i1 < i2) {
            synchronized (o2) {
                Thread.sleep(100);
                synchronized (o1) {
                    System.out.println("o2 -> o1");
                }
            }
        }
    }
}
