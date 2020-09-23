package com.yuansb.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 测试类
 */
@RestController
@RequestMapping("/consumer")
public class ServerConsumerController {

    private final RestTemplate restTemplate;

    @Autowired
    public ServerConsumerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping(value = "/hello")
    public String sayHello() {
        String url = "http://server-provider/provider/hello";
        return restTemplate.getForObject(url, String.class);
    }

    @GetMapping(value = "/echo/{str}")
    public String echo(@PathVariable String str) {
        // 使用服务名请求服务提供者
        String url = "http://server-provider/provider/echo/" + str;
        return restTemplate.getForObject(url, String.class);
    }

}
