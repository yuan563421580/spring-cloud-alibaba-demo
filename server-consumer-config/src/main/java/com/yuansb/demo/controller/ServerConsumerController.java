package com.yuansb.demo.controller;

import com.yuansb.demo.service.ServerConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费接口实现类
 *
 * 通过注解 @RefreshScope 打开动态刷新功能
 */
@RefreshScope
@RestController
@RequestMapping("/consumer")
public class ServerConsumerController {

    @Autowired
    private ServerConsumerService serverConsumerService;

    @Value("${user.name}")
    private String userName;

    @GetMapping(value = "/config/hello")
    public String hello() {
        return serverConsumerService.hello();
    }

    @GetMapping(value = "/config/echo/{str}")
    public String echo(@PathVariable String str) {
        return serverConsumerService.echo(str);
    }

    @GetMapping(value = "/config/echo/userName")
    public String echoUserName() {
        return serverConsumerService.echo(userName);
    }

}
