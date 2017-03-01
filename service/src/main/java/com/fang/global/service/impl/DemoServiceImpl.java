package com.fang.global.service.impl;

import com.modle.User;
import com.fang.global.dao.DemoDao;
import com.fang.service.DemoService;
import com.modle.page.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fn on 2017/2/9.
 */
@Service("demoService")
public class DemoServiceImpl implements DemoService {

    @Resource
    private DemoDao demoDao;

    public DemoDao getDemoDao() {
        return demoDao;
    }

    public void setDemoDao(DemoDao demoDao) {
        this.demoDao = demoDao;
    }

    public String findUserName( ) {
        System.out.println( "" + demoDao );
        List< String > list =  demoDao.findUserNameList(  );
        System.out.println( list );
        return null;
    }
    public List< User > findUserInfo( Page page){
        if( null != page){
            page.setPageNo( page.getPageNo() * page.getPageSize() );
        }
        List< User > userList = demoDao.findUserInfo( page );
        return userList;
    }

    @Transactional
    public String updateUserName(String userId ){
        System.out.println( " 更新用户信息");
        Map< String , String > map = new HashMap<String, String >();
        map.put( "userName" , "test3454563899" );
        map.put( "userId" , userId);
        int id = demoDao.updateUserName( map );
        String str = null;
        str.toString();
        System.out.println( "更新结果:" + id );
        return null;
    }
}
