package com.vergilyn.springboot.demo.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.vergilyn.springboot.demo.mybatis.domain.Hotel;

@Mapper
public interface HotelMapper {

	Hotel selectByCityId(int city_id);

}
