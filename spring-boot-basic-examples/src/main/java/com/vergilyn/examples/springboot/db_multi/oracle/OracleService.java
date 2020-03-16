package com.vergilyn.examples.springboot.db_multi.oracle;

import com.vergilyn.examples.springboot.db_multi.entity.Parent;
import com.vergilyn.examples.springboot.db_multi.mysql.MysqlService;

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
public class OracleService {
    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private OracleDao oracleDao;

    @Transactional(transactionManager = "oracleTS",propagation = Propagation.REQUIRED)
    public Parent getById(int parentId){
        return oracleDao.getById(parentId);
    }

    @Transactional(transactionManager = "oracleTS",rollbackFor = Exception.class)
    public void insert(Parent p) throws Exception{
        oracleDao.insert(p);
    }

    @Transactional(transactionManager = "oracleTS",rollbackFor = Exception.class)
    public void insertDBmulti(Parent parent,boolean isSameTransaction) throws Exception {
        oracleDao.insert(parent);
        if(isSameTransaction){
            mysqlService.insert(parent);
        }else{
            try {
                mysqlService.insert(parent);
            }catch (Exception e){
                e.printStackTrace();;
            }
        }

    }
    @Transactional(transactionManager = "oracleTS",propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void insertREQUIRES_NEW(Parent parent) throws Exception {
        this.insert(parent);
    }
}
