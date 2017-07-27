package com.rpc;

/**
 * Created by yingwuluohan on 2017/6/25.
 */
public class HelloServiceImpl implements HelloService {

    public String hello(String name) {
        return "Hello " + name;
    }

}