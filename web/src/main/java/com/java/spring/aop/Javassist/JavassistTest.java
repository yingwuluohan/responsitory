package com.java.spring.aop.Javassist;


public class JavassistTest {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        //System.out.println("*******************方式一*******************");


        System.out.println("*******************方式二*******************");
        JavassistProxyFactory jpf02 = new JavassistProxyFactory();
        Base a2 = (Base) jpf02.getProxy(Base.class);
        a2.del();
        a2.save();



    }

}
