package com.vergilyn.examples.springboot.mybatis.mapper;

import com.vergilyn.examples.springboot.mybatis.domain.City;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CityMapper {

	City selectByCityId(int city_id);
	
	/**
	 * 注解形式不需要通过dao调用*Mapper.xml。所以可以不写dao、*Mapper.xml、mybatis-config.xml
	 * @param i
	 * @return
	 */
	@Select("select * from city where id = #{id}")
	City selectByAnnotation(int i);
}
