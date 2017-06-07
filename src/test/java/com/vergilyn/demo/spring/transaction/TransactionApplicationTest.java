package com.vergilyn.demo.spring.transaction;

import com.vergilyn.demo.spring.transaction.jdbc.service.JdbcMainService;
import com.vergilyn.demo.spring.transaction.mybatis.service.MybatisMainService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransactionApplication.class)
public class TransactionApplicationTest {
    @Autowired
    private JdbcMainService jdbcMainService;
    @Autowired
    private MybatisMainService mybatisMainService;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Before
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void before() throws Exception {
        this.jdbcTemplate.update("UPDATE parent SET parent_name='周父' WHERE parent_id="+TransactionApplication.ID);
        System.out.println("------------- junit init end -------------");
    }

    @Test
    public void jdbcUpdateCur() throws IOException {
        jdbcMainService.updateCur();
    }

    @Test
    public void jdbcUpdateNew() throws IOException {
        jdbcMainService.updateNew();
    }

    @Test
    public void jdbcGetCur() throws IOException {
        jdbcMainService.getCur();
    }


    @Test
    public void mybatisUpdateCur() throws IOException {
        mybatisMainService.updateCur();
    }

    @Test
    public void mybatisUpdateNew() throws IOException {
        mybatisMainService.updateNew();
    }

    @Test
    public void mybatisGetCur() throws IOException {
        mybatisMainService.getCur();
    }
    @Test
    public void mybatisGetNoCache() throws IOException {
        mybatisMainService.getNoCache();
    }
}
