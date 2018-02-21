package com.zhouchiji.synchronizedBlock;

public class Run {
    public static void main(String[] args) throws InterruptedException {
         new A().start();
         new B().start();
         new C().start();
    }
}
