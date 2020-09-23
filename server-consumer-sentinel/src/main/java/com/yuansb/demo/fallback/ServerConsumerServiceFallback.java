package com.yuansb.demo.fallback;

import com.yuansb.demo.service.ServerConsumerService;
import org.springframework.stereotype.Component;

/**
 * 熔断类
 */
@Component
public class ServerConsumerServiceFallback implements ServerConsumerService {

    @Override
    public String hello() {
        return "hello fallback";
    }

    @Override
    public String echo(String string) {
        return "echo fallback";
    }

}
