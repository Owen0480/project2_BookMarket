package com.springboot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.springboot.repository.mybatis")
public class DBConfig {
    @Bean
    protected DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3307/bookmarketDB");
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("1234");

        return new HikariDataSource(hikariConfig);

    }
    // mybatis 환경 설정 세팅
    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        // mybatis에서 사용할 sqlSession 빈 생성
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        // 1. DataSource 설정
        sqlSessionFactoryBean.setDataSource(dataSource());
        // 2. mapper xml 위치 설정
        sqlSessionFactoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:/mappers/**/*.xml"));

        return sqlSessionFactoryBean.getObject();
    }
}
