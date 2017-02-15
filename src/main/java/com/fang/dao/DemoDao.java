package com.fang.dao;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fn on 2017/2/9.
 */
@Component
public interface DemoDao {

    List< String > findUserNameList(  );


}
