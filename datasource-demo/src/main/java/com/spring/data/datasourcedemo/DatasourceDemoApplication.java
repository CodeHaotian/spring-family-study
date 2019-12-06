package com.spring.data.datasourcedemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Author：Haotian
 * @Date：2019/11/12 20:57
 * @Parameter : @Slf4j 启用Slf4j来记录日志
 * @Parameter ：@Autowired 让 spring 完成 bean 的自动装配的工作。简单来说spring帮我们进行对象创建管理等，让程序员可以直接使用对象。
 */
@SpringBootApplication
@Slf4j
public class DatasourceDemoApplication implements CommandLineRunner {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run( DatasourceDemoApplication.class, args );

    }

    @Override
    public void run(String... args) throws Exception {
        //CommandLineRunner接口，会在spring bean初始化之后，SpringApplication run之前执行，主要用于项目启动前初始化资源文件等，这段初始化代码在整个应用生命周期内只会执行一次。
        showConnection();
        showData();
    }

    /**
     * 查看Spring默认配置数据源信息
     */
    private void showConnection() throws SQLException {
        //在日志中打印使用的默认数据源为HikariDataSource
        log.info( dataSource.toString() );
        Connection conn = dataSource.getConnection();
        //在日志中打印获取到的数据源连接对象
        log.info( conn.toString() );
        conn.close();
    }

    /**
     * 执行查询查看日志打印信息
     */
    private void showData() {
        //进行数据查询，返回List集合，遍历打印到日志当中
        jdbcTemplate.queryForList( "SELECT * FROM Student" )
                .forEach( row -> log.info( row.toString() ) );
    }
}