package com.common.utils.spring;

import com.common.utils.excel.ReadAndWriteExcel;

import java.io.InputStream;

/**
 * Created by   on 2017/2/16.
 */
public class ClassPathResource {

    private Class< ? > classes;
    private String path="";

    

    public ClassPathResource(String path){
        ClassPathResource( path , (ClassLoader) null);
    }
    

    public void ClassPathResource(String path, ClassLoader classLoader) {
        if( classLoader == null ) {
            try {
                classLoader = Thread.currentThread().getContextClassLoader();
            } catch (Throwable ex) {
            }
        }
        this.path = path != null ? path.toLowerCase():"";

    }
    public InputStream getInputStream( String path ){
        InputStream inputStream = classes.getResourceAsStream( path );

        return inputStream;
    }
    public static void main( String[] args ){
         new ClassPathResource( "" );
    }


}
