
package com.vergilyn.springboot.demo.mybatis.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.vergilyn.springboot.demo.mybatis.domain.City;

@Component
public class CityDao {

	private final SqlSession sqlSession;

	public CityDao(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	public City selectCityById(long id) {
		return this.sqlSession.selectOne("selectCityById", id);
	}

}
