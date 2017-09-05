package com.java.spring.aop.Javassist;

/**
 * Created by yingwuluohan on 2017/8/21.
 */
public class Base {

    public String save(){

        System.out.println("保存商品");
        return "save";
    }
    public void del(){
        System.out.println("删除商品");
    }
}
