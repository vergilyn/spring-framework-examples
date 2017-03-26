package com.vergilyn.demo.springboot.db_multi.mysql.services;

import com.vergilyn.demo.bean.database.Parent;
import com.vergilyn.demo.springboot.db_multi.mysql.dao.MysqlDao;
import com.vergilyn.demo.springboot.db_multi.oracle.services.OracleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
@Service
public class MysqlService {
    @Autowired
    private MysqlDao mysqlDao;
    @Autowired
    private OracleService oracleService;

    @Transactional(transactionManager = "mysqlTS",propagation = Propagation.REQUIRED)
    public Parent getById(int parentId){
        return mysqlDao.getById(parentId);
    }

    @Transactional(transactionManager = "mysqlTS",rollbackFor = Exception.class)
    public void insert(Parent p) throws Exception{
        mysqlDao.insert(p);
    }

    @Transactional(transactionManager = "mysqlTS",propagation = Propagation.REQUIRED)
    public void insertREQUIRES_NEW(Parent parent) throws Exception {
        oracleService.insertREQUIRES_NEW(parent);
        this.insert(parent);
    }
}
