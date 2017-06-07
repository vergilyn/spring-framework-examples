package com.vergilyn.demo.spring.transaction.mybatis.service;

import com.vergilyn.demo.bean.database.Parent;
import com.vergilyn.demo.spring.transaction.TransactionApplication;
import com.vergilyn.demo.spring.transaction.mybatis.mapper.MybatisParentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/22
 */
@Service
public class MybatisMainService {
    @Autowired
    private MybatisParentMapper parentMapper;
    @Autowired
    private MybatisNewService newService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void getNoCache(){
        Parent s1 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("s1 select: " + s1);

        // 利用断点: 手动修改数据库的值  '周父' -> '周父s2'
        Parent s2 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("s2 select: " + s2);

        Parent s3 = parentMapper.getNoCacheEntity(TransactionApplication.ID);
        System.out.println("s3 select: " + s3);
        Parent s4 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("s4 select: " + s4);

        // 利用断点: 手动修改数据库的值  '周父' -> '周父s3'
        Parent s5 = parentMapper.getNoCacheEntity(TransactionApplication.ID);
        System.out.println("s5 select: " + s5);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void getCur(){
        Parent s1 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("s1 select: " + s1);

        // 利用断点: 手动修改数据库的值  '周父' -> '周父s2'
        Parent s2 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("s2 select: " + s2);

        // 利用断点: 手动修改数据库的值  '周父' -> '周父s3'
        Parent s3 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("s3 select: " + s3);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCur(){
        Parent init = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("init select: " + init);

        Parent curu = this.updateCur(init);
        System.out.println("curu update: " + curu);

        Parent curs = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("curs select: " + curs);

        Parent news = newService.getEntity(TransactionApplication.ID);
        System.out.println("news select: " + news);

        Parent nocs = parentMapper.getNoCacheEntity(TransactionApplication.ID);
        System.out.println("nocs select: " + nocs);

        Parent curs2 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("curs2 select: " + curs2);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateNew(){
        Parent init = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("init select: " + init);

        Parent newu = newService.updateNew(init);
        System.out.println("newu update: " + newu);

        Parent curs = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("curs select: " + curs);

        Parent news = newService.getEntity(TransactionApplication.ID);
        System.out.println("news select: " + news);

        Parent nocs = parentMapper.getNoCacheEntity(TransactionApplication.ID);
        System.out.println("nocs select: " + nocs);

        Parent curs2 = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("curs2 select: " + curs2);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Parent updateCur(Parent parent){
        Parent p2 = new Parent();
        p2.setParentId(parent.getParentId());
        p2.setParentName(parent.getParentName() + "@cur");
        parentMapper.update(p2);

        return p2;
    }
}
