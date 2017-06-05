package com.fang.global.dao;

import com.modle.User;
import com.modle.page.Page;
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

    /**
     * 分页查询
     * @param page
     * @return
     */
    List<User> findUserInfo(Page page);


    User findUserByMobile(String emailAndMobile);
}
