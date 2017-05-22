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
    public void error(){
        Parent init = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("ini select: " + init);

        Parent newu = newService.updateNew(init);
        System.out.println("new update: " + newu);


        Parent curs = parentMapper.getEntity(TransactionApplication.ID);
        System.out.println("cur select: " + curs);

        Parent news = newService.getEntity(TransactionApplication.ID);
        System.out.println("new select: " + news);
    }
}
