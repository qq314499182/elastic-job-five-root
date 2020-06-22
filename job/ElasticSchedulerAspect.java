package com.tct.iids.config.job;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @Description 定义扫描方法
 * @Author zhaoke
 * @Date 2020/6/1 15:38
 **/
@Slf4j
@Component
public class ElasticSchedulerAspect implements ApplicationContextAware, InitializingBean {

    private static final String PREFIX_PARAMS = "${";

    private static final String SUFFIX_PARAMS = "}";

    private ApplicationContext applicationContext;

    @Resource
    private ElasticJobHandler elasticJobHandler;

    @Override
    public void afterPropertiesSet(){
        registerJob(applicationContext);
    }

    /**
     * 解析context信息，开始注册
     * @param applicationContext  context信息
     */
    private void registerJob(ApplicationContext applicationContext) {
        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(ElasticScheduler.class);
        for (String beanName : beanNamesForAnnotation) {
            Class<?> handlerType = applicationContext.getType(beanName);
            Object bean = applicationContext.getBean(beanName);
            ElasticScheduler annotation = AnnotationUtils.findAnnotation(handlerType, ElasticScheduler.class);
            if(annotation != null){
                this.addJobToContext(annotation,bean);
            }
        }
    }

    /**
     * 将任务添加到容器中
     * @param elasticScheduler 注解对象
     * @param bean job实例
     */
    private void addJobToContext(ElasticScheduler elasticScheduler, Object bean) {
        String cron = this.getValue(elasticScheduler.cron());
        String shardingItemParameters = this.getValue(elasticScheduler.shardingItemParameters());
        Integer shardingTotalCount = Integer.valueOf(this.getValue(elasticScheduler.shardingTotalCount()));
        String jobParameters = this.getValue(elasticScheduler.jobParameters());
        try {
            elasticJobHandler.addJob((ElasticJob) bean,cron,shardingTotalCount,shardingItemParameters,jobParameters);
        } catch (Exception e) {
            log.error("任务添加容器异常:{}",e.getMessage(),e);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    /**
     * 判断是否从配置文件读取job配置
     * @param params 参数
     * @return 解析后参数
     */
    private String getValue(String params){
        if(params.startsWith(PREFIX_PARAMS) && params.endsWith(SUFFIX_PARAMS)){
            String valueName = params.replace(PREFIX_PARAMS, "").replace(SUFFIX_PARAMS, "");
            String value = applicationContext.getEnvironment().getProperty(valueName);
            Assert.notNull(value,String.format("配置文件中未读取到[%s]的值",params));
            return value;
        }else {
            return params;
        }
    }

}
