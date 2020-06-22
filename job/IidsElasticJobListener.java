package com.tct.iids.config.job;

import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Description
 * @Author zhaoke
 * @Date 2020/6/1 14:53
 **/
@Slf4j
public class IidsElasticJobListener implements ElasticJobListener {


    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        log.info("----- 定时任务[{}]开始时间：{}",shardingContexts.getJobName(),nowDate);
        log.info("----- 定时任务任务总片数：{}",shardingContexts.getShardingTotalCount());
        log.info("----- 定时任务当前分片项：{}",shardingContexts.getTaskId());
        log.info("----- 定时任务当前分片参数：{}",shardingContexts.getShardingItemParameters().toString());
        log.info("----- 定时任务任务参数：{}",shardingContexts.getJobParameter());
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        String nowDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"));
        log.info("----- 定时任务[{}]结束时间：{}",shardingContexts.getJobName(),nowDate);
    }
}
