基础：在本地启动nacos，本次环节测试使用1.3.0
    访问地址：http://127.0.0.1:8848/nacos/index.html
    用户名/密码 ： nacos/nacos
一、创建工程：spring-cloud-alibaba-demo
    1.删除 src 文件夹
    2.在pom.xml中设置父类工程为pom方式 : <packaging>pom</packaging>
    3.在pom.xml中配置 spring cloud 和 spring cloud alibaba 的统一版本
        <properties>
            <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
            <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
            <java.version>1.8</java.version>
            <spring-cloud.version>Greenwich.SR2</spring-cloud.version>
            <spring-cloud-alibaba.verion>2.1.0.RELEASE</spring-cloud-alibaba.verion>
            <springboot.version>2.1.6.RELEASE</springboot.version>
        </properties>
        <dependencyManagement>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-dependencies</artifactId>
                    <version>${spring-cloud.version}</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
                <dependency>
                    <groupId>com.alibaba.cloud</groupId>
                    <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                    <version>${spring-cloud-alibaba.verion}</version>
                    <type>pom</type>
                    <scope>import</scope>
                </dependency>
            </dependencies>
        </dependencyManagement>
二、创建服务提供者模块：server-provider
    1.在pom.xml中配置 Nacos的服务注册与发现模块 
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!-- 健康监控 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>
            <!-- Nacos的服务注册与发现模块 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            </dependency>
        </dependencies>
    2.创建配置文件：application.yml
        配置 服务注册中心
            spring:
              application:
                name: server-provider
              cloud:
                nacos:
                  discovery:
                    server-addr: 127.0.0.1:8848
        配置 端点检查（健康检查）
            management:
              endpoints:
                web:
                  exposure:
                    include: "*"
    3.编写创建启动类：ServerProviderApplication
        通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
    4.编写创建暴露服务实现类：ServerProviderController， 实现简单测试逻辑
    5.启动工程，进行本环节测试
        01).查看是否成功注册服务： http://127.0.0.1:8848/nacos/index.html 查看
            登录成功 -> 服务管理 -> 服务列表 -> （找对对应条目点击详情查看端口）
            发现一个服务已经注册在服务中了，服务名为 server-provider , 端口为 8070
        02).测试服务实现类： 
            -通过浏览器访问 http://127.0.0.1:8070/provider/hello 查看
             返回结果正确 ： hello nacos provider, i am from port: 8070
            -通过浏览器访问 http://127.0.0.1:8070/provider/echo/hi 查看
             返回结果正确 ： hello nacos provider hi
        03).服务端点检查
            通过浏览器访问 http://127.0.0.1:8070/actuator/nacos-discovery
            返回结果 
                {
                    "subscribe": [],
                    "NacosDiscoveryProperties": {
                        "serverAddr": "127.0.0.1:8848",
                        "endpoint": "",
                        "namespace": "",
                        "watchDelay": 30000,
                        "logName": "",
                        "service": "server-provider",
                        "weight": 1.0,
                        "clusterName": "DEFAULT",
                        "namingLoadCacheAtStart": "false",
                        "metadata": {
                            "preserved.register.source": "SPRING_CLOUD"
                        },
                        "registerEnabled": true,
                        "ip": "192.168.88.1",
                        "networkInterface": "",
                        "port": 8070,
                        "secure": false,
                        "accessKey": "",
                        "secretKey": "",
                        "heartBeatInterval": null,
                        "heartBeatTimeout": null,
                        "ipDeleteTimeout": null
                    }
                }
    健康检查：以指定方式检查服务下挂载的实例 (Instance) 的健康度，从而确认该实例 (Instance) 是否能提供服务。
        根据检查结果，实例 (Instance) 会被判断为健康或不健康。对服务发起解析请求时，不健康的实例 (Instance) 不会返回给客户端。
    7.启动多个实例方法：
        点开项目运行的配置编辑框 -> Edit Configurations -> 勾选Allow parallel run复选框 
        -> 修改 application.yml 中端口号 -> 点击绿色三角号运行标志启动 -> 运行标志右下角那里会出现数字2   
三、创建服务消费者模块（Ribbon）：server-consumer
    1.在pom.xml中配置 Nacos的服务注册与发现模块 
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!-- 健康监控 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>
            <!-- Nacos的服务注册与发现模块 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            </dependency>
        </dependencies>
    2.创建配置文件：application.yml
        配置 服务注册中心
            spring:
              application:
                name: server-consumer
              cloud:
                nacos:
                  discovery:
                    server-addr: 127.0.0.1:8848
        配置 端点检查（健康检查）
            management:
              endpoints:
                web:
                  exposure:
                    include: "*"
    3.编写创建启动类：ServerConsumerApplication
        通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
    4.编写创建配置类：ConsumerConfiguration
        主要作用是为了注入 RestTemplate
    5.编写创建测试类：ServerConsumerController
        通过服务名 server-provider 方式调用服务提供者测试方法
    6.启动工程，进行本环节测试
         01).查看是否成功注册服务： http://127.0.0.1:8848/nacos/index.html 查看
            登录成功 -> 服务管理 -> 服务列表 -> （找对对应条目点击详情查看端口）
            发现一个服务已经注册在服务中了，服务名为 server-consumer , 端口为 8080
         02).测试服务实现类： 
            -通过浏览器访问 http://127.0.0.1:8080/consumer/hello 查看（刷新页面）
             返回结果正确 ： hello nacos provider, i am from port: 8070
                           hello nacos provider, i am from port: 8071
            -通过浏览器访问 http://127.0.0.1:8080/consumer/echo/hi 查看
             返回结果正确 ： hello nacos provider hi
         03).服务端点检查
            通过浏览器访问 http://127.0.0.1:8080/actuator/nacos-discovery
            返回结果
                {
                    "subscribe": [],
                    "NacosDiscoveryProperties": {
                        "serverAddr": "127.0.0.1:8848",
                        "endpoint": "",
                        "namespace": "",
                        "watchDelay": 30000,
                        "logName": "",
                        "service": "server-consumer",
                        "weight": 1.0,
                        "clusterName": "DEFAULT",
                        "namingLoadCacheAtStart": "false",
                        "metadata": {
                            "preserved.register.source": "SPRING_CLOUD"
                        },
                        "registerEnabled": true,
                        "ip": "192.168.88.1",
                        "networkInterface": "",
                        "port": 8080,
                        "secure": false,
                        "accessKey": "",
                        "secretKey": "",
                        "heartBeatInterval": null,
                        "heartBeatTimeout": null,
                        "ipDeleteTimeout": null
                    }
                }
四、创建服务消费者模块（Feign）：server-consumer-feign
    1.在pom.xml中配置 Nacos的服务注册与发现模块 和 OpenFeign
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!-- 健康监控 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>
            <!-- Nacos的服务注册与发现模块 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            </dependency>
            <!--  Spring Cloud OpenFeign 用于Spring Boot应用程序的声明式REST客户端 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-openfeign</artifactId>
            </dependency>
        </dependencies>
    2.创建配置文件：application.yml
        配置 服务注册中心
            spring:
              application:
                name: server-consumer-feign
              cloud:
                nacos:
                  discovery:
                    server-addr: 127.0.0.1:8848
        配置 端点检查（健康检查）
            management:
              endpoints:
                web:
                  exposure:
                    include: "*"        
    3.编写创建启动类：ServerConsumerFeignApplication
        通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
        通过 @EnableFeignClients 注解开启 Feign 功能
    4.在service文件夹下创建接口 : ServerConsumerService
        通过注解 @FeignClient("server-provider") 要调用的服务名称
        实现对 server-provider 的调用
    5.编写创建消费接口实现类：ServerConsumerController
        通过 ServerConsumerService 调用 实现
    6.启动工程，进行本环节测试
        打开浏览器访问：http://127.0.0.1:8083/consumer/feign/hello， 查看 
        刷新2次，返回结果正确（负载均衡测试通过） ： 
            hello nacos provider, i am from port: 8070
            hello nacos provider, i am from port: 8071
    -
     补充说明：Feign 是一个声明式的伪 HTTP 客户端，它使得写 HTTP 客户端变得更简单。
            使用 Feign，只需要创建一个接口并注解。它具有可插拔的注解特性，可使用 Feign 注解和 JAX-RS 注解。
            Feign 支持可插拔的编码器和解码器。
            Feign 默认集成了 Ribbon，Nacos 也很好的兼容了 Feign，默认实现了负载均衡的效果。
五、创建服务消费者模块（Feign）接入配置中心：server-consumer-config
    1.在pom.xml中配置 Nacos的服务注册与发现模块 、 OpenFeign 和 Config
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!-- 健康监控 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>
            <!-- Nacos的服务注册与发现模块 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            </dependency>
            <!-- Spring Cloud OpenFeign 用于Spring Boot应用程序的声明式REST客户端 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-openfeign</artifactId>
            </dependency>
            <!-- Nacos的分布式配置中心 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            </dependency>
        </dependencies>
    2.使用控制台发布配置：server-consumer-config.yaml
        通过浏览器访问 http://127.0.0.1:8848/nacos/index.html 
        配置管理 -> 配置列表 -> 点击右上角[+]按钮
        查看图文操作可以通过 /doc/nacos.doc 查看
    3.创建配置文件：bootstrap.properties
        -注意： Spring Boot 配置文件的加载顺序，依次为 bootstrap.properties -> bootstrap.yml 
                -> application.properties -> application.yml，其中 bootstrap.properties 配置为最高优先级
    4.编写创建启动类：ServerConsumerConfigApplication
            通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
            通过 @EnableFeignClients 注解开启 Feign 功能
    5.拷贝 server-consumer-feign 中的 service 和 controller
    6.修改 ServerConsumerController
        添加 @RefreshScope 注解，打开动态刷新功能
        @Value 形式获取 userName, 用于测试值是否改变
    7.启动工程，进行本环节测试
        01).打开浏览器访问：http://127.0.0.1:8086/consumer/config/hello， 查看 
            刷新2次，返回结果正确（负载均衡测试通过） ： 
                hello nacos provider, i am from port: 8070
                hello nacos provider, i am from port: 8071
        02).打开浏览器访问：http://127.0.0.1:8086/consumer/config/echo/userName， 查看
            返回结果正确： hello nacos provider TEST_A
            -修改配置：user.name 为 TEST_B
            刷新界面查看：返回结果正确： hello nacos provider TEST_B
        03).Endpoint 信息查看 nacos-discovery：
            http://127.0.0.1:8086/actuator/nacos-discovery
        04).Endpoint 信息查看 nacos-config：
            http://127.0.0.1:8086/actuator/nacos-config
六、创建服务提供者模块接入配置中心（多环境配置）：server-provider-config
    1.在pom.xml中配置 Nacos的服务注册与发现模块 和 Config
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
            </dependency>
            <!-- 健康监控 -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-actuator</artifactId>
            </dependency>
            <!-- Nacos的服务注册与发现模块 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
            </dependency>
            <!-- Nacos的分布式配置中心 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
            </dependency>
        </dependencies>
    2.使用控制台发布配置：
        server-provider-config-dev.yaml  , 开发环境， 端口号：8073 和 8074
        server-provider-config-prod.yaml , 生产环境， 端口号：8076 和 8077
    3.创建配置文件：
        bootstrap.properties        决定使用 dev 或 prod 环境的配置
        bootstrap-dev.properties    开发环境
        bootstrap-prod.properties   生产环境
    4.编写创建启动类：ServerProviderConfigApplication
    5.拷贝模块 server-provider 暴露服务实现类 ServerProviderController
    6.启动工程，进行本环节测试
        01).查看是否成功注册服务： http://127.0.0.1:8848/nacos/index.html 查看
            发现一个服务已经注册在服务中了，服务名为 server-provider-config , 端口为 8073
        02).测试服务实现类： 
            -通过浏览器访问 http://127.0.0.1:8073/provider/hello 查看
             返回结果正确 ： hello nacos provider, i am from port: 8073
            -通过浏览器访问 http://127.0.0.1:8074/provider/echo/hi 查看
             返回结果正确 ： hello nacos provider hi
        03).需要指定启动时加载哪一个配置文件
            - Run -> Edit Configurations -> Active profiles: 添加 prod | dev
            - Terminal -> mvn clean package -> java -jar XXX.jar --spring.profiles.active=prod
            - java -jar server-provider-config-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
            通过浏览器访问 http://127.0.0.1:8076/provider/hello 查看
            返回结果正确 ： hello nacos provider, i am from port: 8076
七、修改服务消费者模块（Feign）接入配置中心：server-consumer-config , 
    改为（多环境配置）, 修改调用服务提供者：server-provider-config
    1.使用控制台发布配置：
        server-consumer-config-dev.yaml  , 开发环境， 端口号：8186 和 8187
        server-consumer-config-prod.yaml , 生产环境， 端口号：8188 和 8189
    2.修改创建配置文件：
        bootstrap.properties        决定使用 dev 或 prod 环境的配置
        bootstrap-dev.properties    开发环境
        bootstrap-prod.properties   生产环境
    3.修改 ServerConsumerService : @FeignClient("server-provider-config")
    4.启动工程，进行本环节测试 （将正在运行的服务先停掉） 
        01).打开浏览器访问：http://127.0.0.1:8186/consumer/config/hello， 查看 
            刷新2次，返回结果正确（负载均衡测试通过） ： 
                hello nacos provider, i am from port: 8077
                hello nacos provider, i am from port: 8076
        02).暂时不需要进行其他测试
--------------
com.yuansb.demo