package com.vergilyn.springboot.demo.db_multi;

import com.vergilyn.demo.bean.database.Parent;
import com.vergilyn.demo.springboot.db_multi.DBmultiApplication;
import com.vergilyn.demo.springboot.db_multi.mysql.services.MysqlService;
import com.vergilyn.demo.springboot.db_multi.oracle.services.OracleService;
import org.ietf.jgss.Oid;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DBmultiApplication.class)
public class DBmultiApplicationTest {
    @Autowired
    private MysqlService mysqlService;
    @Autowired
    private OracleService oracleService;
    private final Parent parent = new Parent(100,"Vergilyn");

    @Test
    public void mysqlInsert() throws Exception {
        mysqlService.insert(parent);
        System.out.println("mysql insert end.");
    }

    @Test
    public void oracleInsert() throws Exception {
        oracleService.insert(parent);
        System.out.println("oracle insert end.");
    }

    @Test
    public void sameTransaction() throws Exception {
        oracleService.insertDBmulti(parent, true);
        System.out.println("sameTransaction() end.");

    }

    @Test
    public void diffTransaction() throws Exception {
        oracleService.insertDBmulti(parent, false);
        System.out.println("diffTransaction() end.");
    }

    /**
     * 在mysql中,先调用oracle,此oracle的事务是：REQUIRES_NEW。
     * 所以,即使在mysql方法中最后被回滚,oracle也被正确insert一行数据。
     * @throws Exception
     */
    @Test
    public void insertREQUIRES_NEW() throws Exception {
        mysqlService.insertREQUIRES_NEW(parent);
        System.out.println("insertREQUIRES_NEW() end.");
    }
}
