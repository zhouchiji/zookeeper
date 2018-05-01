package com;

interface A {
    void a();
}

class _A{

    public int a = 1;

    public void a() {
        System.out.println("_A");
    }
}

class _B extends _A {


    public int a =2;
    public void a() {
        System.out.println("_B");
    }
}

public class Test {
    public static void main(String[] args) {
        _A a = new _B();
        System.out.println(a.a);
        a.a();
    }
}
