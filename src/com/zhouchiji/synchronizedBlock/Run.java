package com.zhouchiji.synchronizedBlock;

public class Run {
    public static void main(String[] args) throws InterruptedException {
       final Common common = new Common();
       Thread a = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   common.A();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });
       Thread b = new Thread(new Runnable() {
           @Override
           public void run() {
               common.A2();
           }
       });

       Thread c = new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   common.B();
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       });

       Thread d = new Thread(new Runnable() {
           @Override
           public void run() {
               common.B2();
           }
       });

       Thread e = new Thread(new Runnable() {
           @Override
           public void run() {
               Common.C();
           }
       });

       a.start();
       b.start();
       c.start();
       d.start();
       e.start();

       //同步代码块this就和同步方法一样，获得的是对象锁。而静态方法获得的是类锁，和对象锁不同
    }
}
