package com.vergilyn.demo.springboot.db_multi.config;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/3/26
 */
@Configuration
@PropertySource("classpath:config/dbMulti/db_multi.properties")
public class DBmultiConfig {
    /* 此处 @Bean + @Qualifier("oracleDB") 等价于 @Bean("oracleDB").
     * 如果写成@Bean("oracleDB"),在idea中,之后的@Qualifier("oracleDB")会有error提示.但不影响代码的正确性.
     */
    @Bean
    @Qualifier("oracleDB")
    @Primary
    @ConfigurationProperties("oracle.datasource")
    public DataSource oracleDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "oracleJT")
    public JdbcTemplate oracleJdbcTemplate(@Qualifier("oracleDB")DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
    @Bean("oracleTS")
    public DataSourceTransactionManager oracleTransactionManager(@Qualifier("oracleDB")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
//  多数据源mybatis的sqlSession注入
//  @Bean("oracleSS")
    public SqlSessionFactory oracleSqlSession(@Qualifier("oracleDB")DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        //bean.setXX(...) 其余mybatis的设置
        /* 例如：以下是mybatis基于*.xml文件配置,如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定）. 则无需set.
         * factoryBean.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage")); // 指定基包
         * factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));
         */
        return factoryBean.getObject();
    }


    @Bean
    @Qualifier("mysqlDB")
    @ConfigurationProperties("mysql.datasource")
    public DataSource mysqlDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mysqlJT")
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDB") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean("mysqlTS")
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDB")DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }



}

