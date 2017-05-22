package com.vergilyn.demo.spring.transaction.jdbc.dao;

import com.vergilyn.demo.bean.database.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/20
 */
@Repository
public class JdbcParentDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedJdbc;

    public Parent getEntity(String id){
        String sql = "select * from parent where parent_id = ?";

        return this.jdbcTemplate.queryForObject(sql,new Object[]{id},new BeanPropertyRowMapper<Parent>(Parent.class));
    }

    public void update(Parent parent){
        String sql = "update parent set parent_name = ?  where parent_id = ?";
        this.jdbcTemplate.update(sql,new Object[]{parent.getParentName(),parent.getParentId()});
    }
}
