package com.tct.iids.config.job;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description 抽象添加job方法
 * @Author zhaoke
 * @Date 2020/6/1 15:32
 **/

@Component
public class ElasticJobHandler {

    @Resource
    private ZookeeperRegistryCenter regCenter;
    /**
     * 监听job启动和完毕时间
     */
    @Resource
    private ElasticJobListener elasticJobListener;

    /**
     * 事件追踪
     */
//    @Resource
//    private JobEventConfiguration jobEventConfiguration;

    /**
     * 抽象添加job方法
     * @param elasticJob 任务实现类
     * @param cron 定时表达式
     * @param shardingTotalCount  分片总数
     * @param shardingItemParameters 分片参数e
     */
    public void addJob(final ElasticJob elasticJob,
                       final String cron,
                       final Integer shardingTotalCount,
                       final String shardingItemParameters,
                       final String jobParameters) throws Exception{
        LiteJobConfiguration jobConfig =
                getLiteJobConfiguration(elasticJob.getClass(), cron, shardingTotalCount, shardingItemParameters,jobParameters);

        new SpringJobScheduler(elasticJob, regCenter, jobConfig, null, elasticJobListener).init();
    }

    /**
     * @Description 任務配置類
     */
    private LiteJobConfiguration getLiteJobConfiguration(final Class<?> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters,
                                                         final String jobParameters) throws Exception{
        JobTypeConfiguration jobTypeConfiguration;
        Object object = jobClass.newInstance();
        if(object instanceof SimpleJob){
            jobTypeConfiguration = new SimpleJobConfiguration(
                    JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                            .shardingItemParameters(shardingItemParameters).jobParameter(jobParameters).build()
                    , jobClass.getCanonicalName());
        }else {
            jobTypeConfiguration = new DataflowJobConfiguration(
                    JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                            .shardingItemParameters(shardingItemParameters).jobParameter(jobParameters).build()
                    ,jobClass.getCanonicalName(),true);
        }
        return LiteJobConfiguration.newBuilder(jobTypeConfiguration)
                //覆盖zookeeper节点信息
                .overwrite(true)
                //默认任务不执行，必须在job console控制台手动启动
                .disabled(true).build();
    }

}
