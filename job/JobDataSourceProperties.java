package com.tct.iids.config.job;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @Description 配置持久化数据源
 * @Author zhaoke
 * @Date 2020/6/1 15:18
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Configuration
@ConfigurationProperties(prefix = "mysql.datasource")
public class JobDataSourceProperties {

    private String url;
    private String username;
    private String password;
    private String driverClassName;

    @Bean(name = "MysqlDataSource")
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
