package com.yuansb.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 *
 * 通过 Spring Cloud 原生注解 @EnableDiscoveryClient 开启服务注册发现功能
 * 通过 @EnableFeignClients 注解开启 Feign 功能
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class ServerConsumerFeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerConsumerFeignApplication.class, args);
    }

}
