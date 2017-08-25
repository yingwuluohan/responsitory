package com.java.spring.selfSchemas;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
public class MyUserNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }
}
