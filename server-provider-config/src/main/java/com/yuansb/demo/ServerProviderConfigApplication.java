package com.yuansb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 启动类
 *
 * 通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ServerProviderConfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerProviderConfigApplication.class, args);
    }

}
