package com.fang.service;

import com.modle.User;
import com.modle.page.Page;

import java.util.List;

/**
 * Created by  on 2017/2/9.
 */
public interface DemoService {

    String findUserName(  );
    User findUser( String emailAndMobile );
    String updateUserName(String userId );

    List< User > findUserInfo(Page page);
}
