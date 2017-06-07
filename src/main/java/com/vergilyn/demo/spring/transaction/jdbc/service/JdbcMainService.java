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
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/20
 */
@Service
public class JdbcMainService {
    @Autowired
    private JdbcParentDao parentDao;
    @Autowired
    private JdbcNewService newService;


    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCur() {
        Parent init = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("init select: " + init);

        Parent curu = this.updateCur(init);
        System.out.println("curu update: " + curu);


        Parent curs = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("curs select: " + curs);

        Parent news = newService.getEntity(TransactionApplication.ID);
        System.out.println("news select: " + news);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateNew() {
        Parent init = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("init select: " + init);

        Parent newu = newService.updateNew(init);
        System.out.println("newu update: " + newu);


        Parent curs = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("curs select: " + curs);

        Parent news = newService.getEntity(TransactionApplication.ID);
        System.out.println("news select: " + news);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void getCur(){
        Parent s1 = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("s1 select: " + s1);

        // 利用断点: 手动修改数据库的值  '周父' -> '周父s2'
        Parent s2 = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("s2 select: " + s2);

        // 利用断点: 手动修改数据库的值  '周父' -> '周父s3'
        Parent s3 = parentDao.getEntity(TransactionApplication.ID);
        System.out.println("s3 select: " + s3);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Parent updateCur(Parent parent) {
        Parent p2 = new Parent();
        p2.setParentId(parent.getParentId());
        p2.setParentName(parent.getParentName() + "@cur");
        parentDao.update(p2);
        return p2;
    }
}
