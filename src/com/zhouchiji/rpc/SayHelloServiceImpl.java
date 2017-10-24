package com.zhouchiji.rpc;

public class SayHelloServiceImpl implements SayHelloService {

    public SayHelloServiceImpl() {
        super();
    }

    @Override
    public String sayHello(String str) {
        if ("hello".equals(str)) {
            return "hello world!";
        } else {
            return "error!";
        }
    }

}