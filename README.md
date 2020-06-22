## elastic-job分布式任务调度框架

## 一、总体架构

![26123315_WkLi](https://img-blog.csdnimg.cn/20200610173929610.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxMzE0NDk5MTgy,size_16,color_FFFFFF,t_70)

## 二、分片概念

1、分片为并发最小执行单元，根据zookeeper选举机制，会在注册的job服务实例中选举实例分配给分片节点，进行任务执行。其他为被分配的实例节点会作为候选节点，在执行节点宕机后，重新进行选举，从而实现好可用。

2、分片机制是为了提高任务执行能力，提高处理数据能力，提高任务系统吞吐量的目的。

3、分片相关参数，分片参数必须与分片数相匹配，否则会导致任务执行失败。

```java
shardingItemParameters: 0=beijing,1=shanghai,2=zuzhou 
```

数字为分片索引，参数为执行分片任务时的参数，可根据实际业务进行并发处理业务数据。

## 三、调度任务类型

1、SimpleJob

简单调度任务，根据时间表达式corn，执行execute方法。

代码实例：

```java
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
```

2、DataflowJob

数据流调度任务，将数据抓取与数据处理分开。数据抓取方法fetchData返回值必须是null或空集合，否则fetchData方法会一直执行。

代码实例:

````java
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
````

## 四、注解与相关配置

1、注解参数说明

| -名称                  | 说明                              | 必填 | -默认值    |
| ---------------------- | --------------------------------- | ---- | ---------- |
| name                   | 任务名称，作为任务唯一key         | 否   | 默认为包名 |
| cron                   | 用于控制作业触发时间              | 是   | ---        |
| shardingTotalCount     | 总分片数                          | 否   | 1          |
| shardingItemParameters | 分片参数 例：0=a,1=b,2=c          | 否   | ---        |
| jobParameters          | 任务参数,每个执行任务都会携带参数 | 否   | ---        |
| description            | 任务描述                          | 否   | ----       |

2、注解支持直接赋值写法,同时支持spring表达式写法

直接写法：

```java
@ElasticScheduler(
        cron = "0/5 * * * * ?",
        shardingTotalCount = "1",
        jobParameters = "a",
        shardingItemParameters = "0=beijing",
        description = "这是一个simpleJob测试类"
)
```

spring表达式写法

```java
@ElasticScheduler(
        cron = "${job.simple.cron}",
        shardingTotalCount = "${job.simple.shardingTotalCount}",
        jobParameters = "${job.simple.jobParameters}",
        shardingItemParameters = "${job.simple.shardingItemParameters}",
        description = "job.simple.description"
)
```

配置文件

```yaml
job:
  simple:
    cron: 0/5 * * * * ?
    shardingTotalCount: 1
    jobParameters: test
    shardingItemParameters: 0=beijing
    description: 这是一个基础任务
  dataflow:
    cron: 0/5 * * * * ?
    shardingTotalCount: 3
    jobParameters: test
    shardingItemParameters: 0=beijing,1=shanghai,2=zuzhou
    description: 这是一个数据流任务
```

## 五、调度任务控制台

1、调度任务控制太为单独部署服务，elastic-job-console.下载压缩包后，在服务器解压。打开bin目录，执行r如下命令，默认端口8899，打开浏览器http://服务器IP:8899   **http://49.234.28.46:8899/#**，

```shell
./start.sh -p 端口号  
```

链接：https://pan.baidu.com/s/1jlc6Bv0L_6ktWDpASaCtdA 
提取码：b05f 

注：系统默认提供2个账号   root/root     guest/guest

2、控制台操作

配置注册中心：

![123](https://ftp.bmp.ovh/imgs/2020/06/aebea4fe63bb4241.png)

![1111](https://ftp.bmp.ovh/imgs/2020/06/8ec9c2f8dd097e52.png)

操作调度任务：

![111](https://ftp.bmp.ovh/imgs/2020/06/b221aa80a4ac055b.png)









