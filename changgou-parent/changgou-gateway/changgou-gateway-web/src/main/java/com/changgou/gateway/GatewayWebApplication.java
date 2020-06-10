package com.changgou.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @File: GatewayWebApplication
 * @Description:
 * @Author: tom
 * @Create: 2020-06-10 11:36
 **/
@SpringBootApplication
@EnableEurekaClient
@Slf4j
public class GatewayWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayWebApplication.class);
    }

    /**
     * 创建用户唯一标识，使用 IP 作为用户的唯一标识，根据 IP 进行限流操作
     * @return
     */
    @Bean("ipKeyResolver")
    public KeyResolver userKeyResolver() {
        return new KeyResolver() {
            @Override
            public Mono<String> resolve(ServerWebExchange exchange) {
//                return Mono.just("需要使用的用户身份识别唯一标识");
                String ip = exchange.getRequest().getRemoteAddress().getHostName();
                log.info("用户请求的IP为：" + ip);
                return Mono.just(ip);
            }
        };
    }
}
