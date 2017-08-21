package com.java.spring.aop.cgLib;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/*
 * 实现了方法拦截器接口
 */
public class Hacker implements MethodInterceptor {
    /**
     *
     * @param obj
     * @param method
     * @param args
     * @param proxy
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        System.out.println("**** 拦截器之前 ***");
        proxy.invokeSuper(obj, args);
        System.out.println("**** 拦截器之后");
        return null;
    }
}
