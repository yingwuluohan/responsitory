package com.rpc;

/**
 * Created by yingwuluohan on 2017/6/25.
 */
public class RpcProvider {

    public static void main(String[] args) throws Exception {
        HelloService service = new HelloServiceImpl();
        RpcFramework.export(service, 1234);
    }

}
