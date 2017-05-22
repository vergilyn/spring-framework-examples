package com.vergilyn.demo.spring.transaction.jdbc.service;

import com.vergilyn.demo.bean.database.Parent;
import com.vergilyn.demo.spring.transaction.TransactionApplication;
import com.vergilyn.demo.spring.transaction.jdbc.dao.JdbcParentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/20
 */
@Service
public class JdbcMainService {
    @Autowired
    private JdbcParentDao parentDao;
    @Autowired
    private JdbcNewService newService;



    @Transactional(propagation = Propagation.REQUIRED)
    public void error(){
        Parent init = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("ini select: " + init);

        Parent newu = newService.updateNew(init);
        System.out.println("new update: " + newu);


        Parent curs = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("cur select: " + curs);

        Parent news = newService.getEntity(TransactionApplication.ID);
        System.out.println("new select: " + news);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void right(){
        Parent p1 = parentDao.getEntity(TransactionApplication.ID); // 1: 周父
        System.out.println(p1);

        Parent p2 = newService.updateNew(p1);   // 1: 周父1
        System.out.println(p2);


        Parent p3 = newService.getEntity(TransactionApplication.ID); //期望 >> 1: 周父-1  实际 >> 1: 周父
        System.out.println(p3);
    }
}
