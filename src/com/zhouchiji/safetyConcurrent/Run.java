package com.zhouchiji.safetyConcurrent;

public class Run {
    public static void main(String[] args) {
        for (int i = 0; i<100 ; i++) {
            new A().start();
            new B().start();
        }
    }
}
