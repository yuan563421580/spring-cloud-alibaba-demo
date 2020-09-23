package com.yuansb.demo.configure;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * 创建一个 Java 配置类，主要作用是为了注入 RestTemplate
 *
 *  spring cloud ribbon客户端负载均衡的时候，可以给 RestTemplate bean 加一个 @LoadBalanced 注解。
 *  @LoadBalanced 注释说：这个注解是用来给RestTemplate做标记，以使用LoadBalancerClient来配置它。
 *  @LoadBalanced 注解，就能让这个RestTemplate在请求时拥有客户端负载均衡的能力（开启负载均衡的功能）。
 */
@Configuration
public class ConsumerConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
