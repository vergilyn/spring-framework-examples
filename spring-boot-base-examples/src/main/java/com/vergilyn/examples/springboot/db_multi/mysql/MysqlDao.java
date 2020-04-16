package com.vergilyn.examples.springboot.db_multi.mysql;

import com.vergilyn.examples.springboot.db_multi.entity.Parent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author VergiLyn
 * @date 2017/3/26
 */
@Repository("mysqlDao")
public class MysqlDao {

//  @Resource(name = "mysqlJT") 等价于 @Qualifier("mysqlJT") + @Autowired
//  Resource是j2ee提供的,而Autowired、Qualifier是由spring提供的.为了降低与spring的耦合度,建议用Resource.
    @Qualifier("mysqlJT")
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Parent getById(int parentId) {
        String sql = "select * from mysql_parent where id = ?";

        return jdbcTemplate.queryForObject(sql,new Object[]{parentId}
                        ,new BeanPropertyRowMapper<Parent>(Parent.class));
    }

    public void insert(Parent p) {
        String sql = "insert into mysql_parent(parent_id,parent_name) values(?,?)";

        jdbcTemplate.update(sql,new Object[]{p.getParentId(),p.getParentName()});
    }
}
