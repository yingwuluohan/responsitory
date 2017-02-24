package com.fang.global.service.impl;

import com.common.utils.modle.User;
import com.fang.global.dao.DemoDao;
import com.fang.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
}
