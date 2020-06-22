package com.tct.iids.config.job;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description
 * @Author zhaoke
 * @Date 2020/6/1 15:29
 **/

@Component
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticScheduler {

    /**
     * 任务名称
     */
//    String name();

    /**
     * cron表达式，用于控制作业触发时间
     */
    String cron();

    /**
     * 总分片数
     */
    String shardingTotalCount() default "1";

    /**
     * 分片参数
     */
    String shardingItemParameters() default "";

    /**
     * 任务描述信息
     */
    String description() default "";

    /**
     * 任务参数
     */
    String jobParameters() default "";
}
