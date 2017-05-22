package com.vergilyn.demo.spring.transaction.mybatis.service;

import com.vergilyn.demo.bean.database.Parent;
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
public class MybatisNewService {
    @Autowired
    private MybatisParentMapper parentMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Parent updateNew(Parent parent){
        Parent p2 = new Parent();
        p2.setParentId(parent.getParentId());
        p2.setParentName(parent.getParentName() + "@new");
        parentMapper.update(p2);

        return p2;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Parent getEntity(String id) {
        return parentMapper.getEntity(id);
    }
}
