/**
 *    Copyright 2015-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.vergilyn.examples.springboot.mybatis;

import com.vergilyn.examples.springboot.mybatis.dao.CityDao;
import com.vergilyn.examples.springboot.mybatis.mapper.CityMapper;
import com.vergilyn.examples.springboot.mybatis.mapper.HotelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * XXX 2020-01-26 重新整理后未验证，估计有错误！
 * @author vergilyn
 * @date 2020-01-26
 */
@SpringBootApplication
public class MybatisApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MybatisApplication.class);
		app.setAdditionalProfiles("mybatis");
		app.run(args);
	}
	@Autowired
	private CityDao cityDao;
	@Autowired
	private HotelMapper hotelMapper;
	@Autowired
	private CityMapper cityMapper;


	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.cityDao.selectCityById(1));
		System.out.println(this.cityMapper.selectByAnnotation(1));
		System.out.println(this.hotelMapper.selectByHotelId(1));
	}

}
