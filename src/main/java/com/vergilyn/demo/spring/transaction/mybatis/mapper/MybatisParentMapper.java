package com.vergilyn.demo.spring.transaction.mybatis.mapper;

import com.vergilyn.demo.bean.database.Parent;
import org.apache.ibatis.annotations.*;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/23
 */
@Mapper
public interface MybatisParentMapper {

    @Select("select * from parent where PARENT_ID = #{id}")
    @Results({
            @Result(id = true, column = "PARENT_ID", property = "parentId"),
            @Result(column = "PARENT_NAME", property = "parentName")
    })
    Parent getEntity(@Param("id") String id);

    @Select("select * from parent where PARENT_ID = #{id}")
    @Results({
            @Result(id = true, column = "PARENT_ID", property = "parentId"),
            @Result(column = "PARENT_NAME", property = "parentName")
    })
    @Options(flushCache = Options.FlushCachePolicy.TRUE,useCache = false)
    Parent getNoCacheEntity(@Param("id") String id);

    @Update("update parent set PARENT_NAME=#{parentName} where PARENT_ID = #{parentId}")
    @Results({
            @Result(id = true, column = "PARENT_ID", property = "parentId"),
            @Result(column = "PARENT_NAME", property = "parentName")
    })
    void update(Parent parent);
}
