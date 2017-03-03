package com.vergilyn.demo.springboot.db.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/* Spring的JdbcTemplate和NamedParameterJdbcTemplate类是被自动配置的，
 * 你可以在自己的beans中通过@Autowire直接注入它们。
 */
@Repository
public class JdbcTemplateDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbc;

	@SuppressWarnings("unused")
	public Long testDao(){
		Map<String,Object> param = new HashMap<String,Object>();
		String sql = " select count(*) from child";
		if(true){
			sql += "  where child_name = :name ";
			param.put("name", "1");
		}
		Long count = this.namedJdbc.queryForObject(sql, param, Long.class);
		System.out.println(count);
		return count;
	}
}
