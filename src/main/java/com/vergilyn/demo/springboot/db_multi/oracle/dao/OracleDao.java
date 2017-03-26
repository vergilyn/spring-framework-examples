package com.vergilyn.demo.springboot.db_multi.oracle.dao;

import com.vergilyn.demo.bean.database.Parent;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
@Repository
public class OracleDao {
    @Resource(name = "oracleJT")
    private JdbcTemplate jdbcTemplate;

    public Parent getById(int parentId) {
        String sql = "select * from oracle_parent where id = ?";

        return jdbcTemplate.queryForObject(sql,new Object[]{parentId}
                ,new BeanPropertyRowMapper<Parent>(Parent.class));
    }

    public void insert(Parent p) {
        String sql = "insert into oracle_parent(parent_id,parent_name) values(?,?)";

        jdbcTemplate.update(sql,new Object[]{p.getParentId(),p.getParentName()});
    }
}
