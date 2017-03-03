package com.vergilyn.demo.springboot.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.vergilyn.demo.springboot.mybatis.domain.City;
import com.vergilyn.demo.springboot.mybatis.domain.Hotel;

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
