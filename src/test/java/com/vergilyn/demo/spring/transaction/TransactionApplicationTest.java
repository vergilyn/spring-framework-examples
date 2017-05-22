package com.vergilyn.demo.spring.transaction;

import com.vergilyn.demo.spring.transaction.jdbc.service.JdbcMainService;
import com.vergilyn.demo.spring.transaction.mybatis.service.MybatisMainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/21
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
public class TransactionApplicationTest {
    @Autowired
    private JdbcMainService jdbcMainService;
    @Autowired
    private MybatisMainService mybatisMainService;

    @Test
    public void jdbcErrorTest() throws IOException {
        jdbcMainService.error();
    }

    @Test
    public void mybatisErrorTest() throws IOException {
        mybatisMainService.error();
    }
}
