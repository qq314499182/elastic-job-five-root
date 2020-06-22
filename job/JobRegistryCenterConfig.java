package com.tct.iids.config.job;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @Description
 * @Author zhaoke
 * @Date 2020/6/1 14:51
 **/

@Configuration
public class JobRegistryCenterConfig {

    @Resource(name = "MysqlDataSource")
    private DataSource dataSource;

    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter regCenter(@Value("${zookeeper.regCenter.serverList}") final String serverList,
                                             @Value("${zookeeper.regCenter.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }

    /**
     * 配置任务监听器
     * @return ElasticJobListener
     */
    @Bean
    public ElasticJobListener elasticJobListener() {
        return new IidsElasticJobListener();
    }

    /**
     * 将作业运行的痕迹进行持久化到DB
     */
//    @Bean
    public JobEventConfiguration jobEventConfiguration(){
        return new JobEventRdbConfiguration(dataSource);
    }

}
