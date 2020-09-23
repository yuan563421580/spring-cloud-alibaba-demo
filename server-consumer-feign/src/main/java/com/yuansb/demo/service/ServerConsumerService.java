package com.yuansb.demo.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 这个接口相当于把原来的服务提供者项目当成一个 Service 类
 *
 *  通过注解 @FeignClient("server-provider") 要调用的服务名称
 */
@FeignClient("server-provider")
public interface ServerConsumerService {

    @GetMapping("/provider/hello")
    public String hello();

    @GetMapping("/provider/echo/{string}")
    public String echo(@PathVariable("string") String string);

}
