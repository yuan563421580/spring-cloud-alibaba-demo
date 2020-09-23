package com.yuansb.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 暴露服务实现类
 * 一个 RestController 提供 RESTFul API 用于测试提供者
 */
@RestController
@RequestMapping("/provider")
public class ServerProviderController {

    @Value("${server.port}")
    private String port;

    /**
     * 普通测试方法
     * @return
     */
    @GetMapping("/hello")
    public String sayHello() {
        return "hello nacos provider, i am from port: " + port;
    }

    /**
     * 一个可以抛出运行时异常的错误测试方法
     * @param string
     * @return
     */
    @GetMapping(value = "/echo/{string}")
    public String echo(@PathVariable String string) {
        if ("fail".equalsIgnoreCase(string)) {
            throw new RuntimeException("FAIL");
        }
        return "hello nacos provider " + string;
    }

}
