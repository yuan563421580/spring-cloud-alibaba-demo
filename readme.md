一、创建工程：spring-cloud-alibaba-demo
二、创建服务提供者模块：server-provider
三、创建服务消费者模块（Ribbon）：server-consumer
四、创建服务消费者模块（Feign）：server-consumer-feign
五、创建服务消费者模块（Feign）接入配置中心：server-consumer-config
六、创建服务提供者模块接入配置中心（多环境配置）：server-provider-config
七、修改服务消费者模块（Feign）接入配置中心：server-consumer-config,改为（多环境配置）,修改调用服务提供者：server-provider-config
八、创建服务消费者模块（Feign）, Sentinel 客户端接入,使用本地配置文件：server-consumer-sentinel