package com.java.statck;

/**
 * Created by yingwuluohan on 2017/8/28.
 */
public class MyArrayStack< T > implements MyStack<T >  {

    private int size;
    private Object[] objs = new Object[16];

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        objs = null;
        size = 0;
    }

    @Override
    public int length() {
        return size;
    }

    @Override
    public boolean push(T data) {
        if( size >= objs.length ){
            //扩容
            extendArray( );
        }
        objs[ size ] = data;
        return true;
    }

    @Override
    public T pop() {
        if( size == 0 ){
            return null;
        }
        return ( T ) objs[ size-- ];
    }

    /**
     * 扩容
     */
    public void extendArray( ){
        Object[] temp = new Object[ objs.length * 2 ];
        for( int i = 0 ; i < temp.length;i++ ){
            temp[ i ] = objs[ i ];
        }
        objs = temp;

    }





}
