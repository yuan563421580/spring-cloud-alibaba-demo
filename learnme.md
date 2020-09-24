外部服务1：在本地启动nacos，本次学习环节测试使用1.3.0
            访问地址：http://127.0.0.1:8848/nacos/index.html
            用户名/密码 ： nacos/nacos
外部服务2：在本地启动sentinel，本次学习环节测试使用1.8.0
            访问地址：http://localhost:8888/#/login
            用户名/密码 ： sentinel/sentinel
---------------------------------------------------------------------------------------
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
八、创建服务消费者模块（Feign）, Sentinel 客户端接入,使用本地配置文件：server-consumer-sentinel
    1.在pom.xml中配置 Nacos的服务注册与发现模块 、 OpenFeign 和 Sentinel
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
            <!-- Alibaba Sentinel 用于以流量为切入点，从流量控制、熔断降级、系统负载保护等 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
            </dependency>
        </dependencies>
    2.创建配置文件：application.yml , 拷贝 server-consumer-feign 模块的配置文件进行修改
        配置 服务注册中心 和 熔断限流
            spring:
              application:
                # 服务名
                name: server-consumer-feign-sentinel
              cloud:
                nacos:
                  discovery:
                    server-addr: 127.0.0.1:8848
                sentinel:
                  transport:
                    dashboard: 127.0.0.1:8888
        配置 开启 Feign 对 Sentinel 的支持
            feign:
              sentinel:
                enabled: true
    3.编写创建启动类：ServerConsumerSentinelApplication, 拷贝 server-consumer-feign 模块的启动类
            通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
            通过 @EnableFeignClients 注解开启 Feign 功能
    4.拷贝 server-consumer-feign 中的 service 和 controller
    5.在 fallback 文件夹下创建熔断类：ServerConsumerServiceFallback
        增加 @Component 注解 、 实现 ServerConsumerService 接口
    6.修改 ServerConsumerService 类
        注解 @FeignClient 增加 fallback 属性指定熔断类
    7.启动工程，进行本环节测试
        01).打开浏览器访问：http://127.0.0.1:8183/consumer/feign/hello， 查看 
            刷新2次，返回结果正确（负载均衡测试通过） ： 
            hello nacos provider, i am from port: 8070
            hello nacos provider, i am from port: 8071
        02).打开浏览器访问：http://127.0.0.1:8183/consumer/feign/echo/fail， 查看 
            返回结果正确（熔断降级测试通过） ： echo fallback
        03).停止 provider 服务， 打开浏览器访问：http://127.0.0.1:8183/consumer/feign/hello， 查看 
            返回结果正确（熔断降级测试通过） ： hello fallback
    -说明：实际可以在 nacos 进行 application.yml 配置
九、创建网关服务模块：server-gateway
    1.在pom.xml中配置 Nacos的服务注册与发现模块 、 gateway 和 Servlet 支持
        <dependencies>
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
            <!-- Alibaba Sentinel 用于以流量为切入点，从流量控制、熔断降级、系统负载保护等 -->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
            </dependency>
            <!-- 网关服务 -->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-gateway</artifactId>
            </dependency>
            <!-- Servlet 支持 -->
            <dependency>
                <groupId>javax.servlet</groupId>
                <artifactId>javax.servlet-api</artifactId>
            </dependency>
        </dependencies>
    -说明：Spring Cloud Gateway 不使用 Web 作为服务器，而是使用 WebFlux 作为服务器，
            Gateway 项目已经依赖了 starter-webflux ， 所以这里千万不要依赖 starter-web 。
            由于过滤器等功能依然需要 Servlet 支持，故这里还需要依赖 javax.servlet:javax.servlet-api 。
    2.创建配置文件：application.yml
        spring:
          application:
            # 应用名称
            name: server-gateway
          cloud:
            # 使用 Nacos 作为服务注册发现
            nacos:
              discovery:
                server-addr: 127.0.0.1:8848
            # 使用 Sentinel 作为熔断器
            sentinel:
              transport:
                dashboard: 127.0.0.1:8888
            # 路由网关配置
            gateway:
              # 设置与服务注册发现组件结合，这样可以采用服务名的路由策略
              discovery:
                locator:
                  enabled: true
              # 配置路由规则
              routes:
                # 采用自定义路由 ID（有固定用法，不同的 id 有不同的功能，详见：https://cloud.spring.io/spring-cloud-gateway/2.0.x/single/spring-cloud-gateway.html#gateway-route-filters）
                - id: SERVER-CONSUMER-FEIGN
                  # 采用 LoadBalanceClient 方式请求，以 lb:// 开头，后面的是注册在 Nacos 上的服务名
                  uri: lb://server-consumer-feign
                  # Predicate 翻译过来是“谓词”的意思，必须，主要作用是匹配用户的请求，有很多种用法
                  predicates:
                    # Method 方法谓词，这里是匹配 GET 和 POST 请求
                    - Method=GET,POST 
    3.编写创建启动类：ServerGatewayApplication
        通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
        通过 @EnableFeignClients 注解开启 Feign 功能
    4.启动工程，进行本环节测试
        打开浏览器访问：http://127.0.0.1:9000/server-consumer-feign/consumer/feign/hello， 查看
            刷新2次，返回结果正确（负载均衡测试通过） ： 
            hello nacos provider, i am from port: 8070
            hello nacos provider, i am from port: 8071
    5.修改application.yml
        修改配置路由规则相关部分：增加 predicates.Path 和 filters.StripPrefix
              # 配置路由规则
              routes:
                # 采用自定义路由 ID（有固定用法，不同的 id 有不同的功能，详见：https://cloud.spring.io/spring-cloud-gateway/2.0.x/single/spring-cloud-gateway.html#gateway-route-filters）
                - id: SERVER-CONSUMER-FEIGN
                  # 采用 LoadBalanceClient 方式请求，以 lb:// 开头，后面的是注册在 Nacos 上的服务名
                  uri: lb://server-consumer-feign
                  # Predicate 翻译过来是“谓词”的意思，必须，主要作用是匹配用户的请求，有很多种用法
                  predicates:
                    # Method 方法谓词，这里是匹配 GET 和 POST 请求
                    - Method=GET,POST
                    # 路径匹配，以 api 开头，直接配置是不生效的，看 filters 配置
                    - Path=/api/**
                  filters:
                    # 前缀过滤，默认配置下，我们的请求路径是 http://localhost:9000/server-consumer-feign/** 这时会路由到指定的服务
                    # 此处配置去掉 1 个路径前缀，再配置上面的 Path=/api/**，就能按照 http://localhost:9000/api/** 的方式访问了
                    - StripPrefix=1
    6.重新启动工程，进行本环节测试
        打开浏览器访问：http://127.0.0.1:9000/api/consumer/feign/hello， 查看
            刷新2次，返回结果正确（负载均衡测试通过） ： 
            hello nacos provider, i am from port: 8070
            hello nacos provider, i am from port: 8071
    7.修改application.yml
        配置路由规则再增加一个id : SERVER-CONSUMER-FEIGN-SENTINEL
              routes:
                # 采用自定义路由 ID（有固定用法，不同的 id 有不同的功能，详见：https://cloud.spring.io/spring-cloud-gateway/2.0.x/single/spring-cloud-gateway.html#gateway-route-filters）
                - id: SERVER-CONSUMER-FEIGN
                  # 采用 LoadBalanceClient 方式请求，以 lb:// 开头，后面的是注册在 Nacos 上的服务名
                  uri: lb://server-consumer-feign
                  # Predicate 翻译过来是“谓词”的意思，必须，主要作用是匹配用户的请求，有很多种用法
                  predicates:
                    # Method 方法谓词，这里是匹配 GET 和 POST 请求
                    - Method=GET,POST
                    # 路径匹配，以 api 开头，直接配置是不生效的，看 filters 配置
                    - Path=/api/**
                  filters:
                    # 前缀过滤，默认配置下，我们的请求路径是 http://localhost:9000/server-consumer-feign/** 这时会路由到指定的服务
                    # 此处配置去掉 1 个路径前缀，再配置上面的 Path=/api/**，就能按照 http://localhost:9000/api/** 的方式访问了
                    - StripPrefix=1
                - id: SERVER-CONSUMER-FEIGN-SENTINEL
                  uri: lb://server-consumer-feign-sentinel
                  predicates:
                    - Path=/business/**
                  filters:
                    - StripPrefix=1
    8.重新启动工程，进行本环节测试
        01).重复步骤6，测试通过
        02).打开浏览器访问：http://127.0.0.1:9000/business/consumer/feign/hello， 查看
            返回结果正确 ： hello nacos provider, i am from port: 8071
    -说明：实际可以在 nacos 进行 application.yml 配置
---------------------------------------------------------------------------------------
com.yuansb.demo