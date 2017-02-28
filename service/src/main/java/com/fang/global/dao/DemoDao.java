package com.fang.global.dao;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2017/2/9.
 */
@Component
public interface DemoDao {

    List< String > findUserNameList();

    int updateUserName( Map<String ,String > map );
    
}
