package com.vergilyn.examples.springboot.mybatis.mapper;

import com.vergilyn.examples.springboot.mybatis.domain.Hotel;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface HotelMapper {

	Hotel selectByHotelId(int city_id);

}
