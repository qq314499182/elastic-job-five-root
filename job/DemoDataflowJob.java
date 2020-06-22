package com.tct.iids.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.google.common.collect.Lists;
import com.tct.iids.config.job.ElasticScheduler;
import com.tct.model.vo.Message;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @Description
 * @Author zhaoke
 * @Date 2020/6/9 11:51
 **/
@Slf4j
@ElasticScheduler(
        cron = "${job.dataflow.cron}",
        shardingTotalCount = "${job.dataflow.shardingTotalCount}",
        jobParameters = "${job.dataflow.jobParameters}"
)
public class DemoDataflowJob implements DataflowJob<Message> {

    @Override
    public List<Message> fetchData(ShardingContext shardingContext) {
        return Lists.newArrayList(new Message());
    }

    @Override
    public void processData(ShardingContext shardingContext, List<Message> list) {
        log.info("执行抓取数据处理");
    }
}
