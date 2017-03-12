package com.vergilyn.demo.springboot.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.vergilyn.demo.springboot.mybatis.domain.Hotel;

@Mapper
public interface HotelMapper {

	Hotel selectByHotelId(int city_id);

}
