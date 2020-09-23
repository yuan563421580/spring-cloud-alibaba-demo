package com.yuansb.demo.controller;

import com.yuansb.demo.service.ServerConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费接口实现类
 */
@RestController
@RequestMapping("/consumer")
public class ServerConsumerController {

    @Autowired
    private ServerConsumerService serverConsumerService;

    @GetMapping(value = "/feign/hello")
    public String hello() {
        return serverConsumerService.hello();
    }

    @GetMapping(value = "/feign/echo/{str}")
    public String echo(@PathVariable String str) {
        return serverConsumerService.echo(str);
    }

}
