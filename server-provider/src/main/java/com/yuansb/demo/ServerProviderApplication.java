package com.yuansb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * 通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
 *      由于引入了spring-cloud-starter-alibaba-nacos-discovery模块，
 *      所以Spring Cloud Common中定义的那些与服务治理相关的接口将使用Nacos的实现
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ServerProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerProviderApplication.class, args);
    }

}
