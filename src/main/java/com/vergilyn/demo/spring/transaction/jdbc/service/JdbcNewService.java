package com.vergilyn.demo.spring.transaction.jdbc.service;

import com.vergilyn.demo.bean.database.Parent;
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
public class JdbcNewService {
    @Autowired
    private JdbcParentDao jdbcParentDao;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Parent updateNew(Parent parent){
        Parent p2 = new Parent();
        p2.setParentId(parent.getParentId());
        p2.setParentName(parent.getParentName() + "@new");
        jdbcParentDao.update(p2);

        return p2;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Parent getEntity(String id) {
        return jdbcParentDao.getEntity(id);
    }
}
