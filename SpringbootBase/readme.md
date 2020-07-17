# 使用Springboot搭建基础开发环境
集成:
>- 集成Mybatis
>- 集成Mybatis自动化生成工具
>- 封装集成Base类,简化开发
>- 集成全局异常处理
>- 集成请求日志打印和日志跟踪
>- 集成调用过程打印
>- 开发集成@CheckNull注解
>- 集成SpringRedis
>- 集成Rabbitmq
>- 集成Elasticsearch
>- 集成excel导入导出
>- 集成pdf模板生成工具



# mybatis
1. 实体类在 `com.lyf.domain`
2. xml文件在 `resources\mapper`

# mybatis自动化生成工具
1. 配置文件在 `resources\generatorConfig.xml`
2. 生成 po dao xml 在 `test.java.com.lyf` 避免影响代码

# 集成封装base类,简化开发
1. 基础封装类在 `com.lyf.base`
2. 解决最基本crud以及分页问题,"0代码"完成单表crud以及分页
3. 查询时,对象必须继承 `BaseQuery.java`放在 `com.lyf.domain.query`

# 集成全局异常处理
1. 文件在 `com.lyf.exception`
2. 自定义参数异常`ParamsException`和断言`AssertUtil`
2. 事务回滚加注解 `@Transactional`

# 集成日志打印
1. 记录每次请求所有信息 `com.lyf.aop.WebLogAspect`
2. 增加日志trace_uuid方便定位操作链路 `resources\logback-spring.xml`
3. 在aop入口进行标识 `MDC.put("trace_uuid", UUID.randomUUID().toString());`

# 集成调用打印
1. 记录方法被调用的方法链 `com.lyf.annotation.LogTrace`
2. 使用加注解 `@LogTrace`

# 开发集成@CheckNull注解
1. 自定义注解@CheckNull校验非空参数
2. 切面类 `CheckNullAspect`

# 集成 SpringRedis
1. 配置文件在 `com.lyf.redis.RedisConfig`
2. 封装RedisUtil `com.lyf.redis.RedisUtil`
3. 集成SpringCache,支持对key指定过期时间

# 集成Rabbitmq
1. 配置文件在 `com.lyf.rabbit.RabbitConfig`
2. 定义消息队列,指定ack方式,定义死信队列

# 集成Elasticsearch
1. 使用 `spring-boot-starter-data-elasticsearch` 集成
2. 配置类 `com.lyf.domain.elastic.GoodsDoc` 定义索引

# 集成excel导入导出
1. `com.lyf.util.ExcelUtil`工具类
2. `@ExcelFiled`注解指定Bean与excel列的对应关系(这种思想很赞!!!)

# 集成pdf模板生成工具
1. `com.lyf.util.PdfUtil`工具类
2. `templates/zhengshu.ftl`通过freemarker模板生成对应的pdf