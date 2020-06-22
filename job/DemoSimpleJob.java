package com.tct.iids.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.tct.iids.config.job.ElasticScheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Description job demo
 * @Author zhaoke
 * @Date 2020/6/1 15:44
 **/
@Slf4j
@ElasticScheduler(
        cron = "${job.simple.cron}",
        shardingTotalCount = "${job.simple.shardingTotalCount}",
        jobParameters = "${job.simple.jobParameters}",
        shardingItemParameters = "${job.simple.shardingItemParameters}",
        description = "这是一个simpleJob测试类"
)
public class DemoSimpleJob implements SimpleJob {

    @Value("${server.port}")
    private String port;

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("当前线程:{}",Thread.currentThread().getName());
        log.info("当前任务分片端口:{}",port);
        log.info("分片数:{}",shardingContext.getShardingItem());
        log.info("分片参数:{}",shardingContext.getShardingParameter());
    }
}
