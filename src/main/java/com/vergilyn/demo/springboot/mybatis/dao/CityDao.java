
package com.vergilyn.demo.springboot.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vergilyn.demo.springboot.mybatis.domain.City;

@Component
public class CityDao {
	@Autowired
	private SqlSession sqlSession;

	public City selectCityById(long id) {
		return this.sqlSession.selectOne("selectCityById", id);
	}

}
