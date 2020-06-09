package com.changgou.item;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @File: ItemApplication
 * @Description:
 * @Author: tom
 * @Create: 2020-06-09 15:50
 **/
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.changgou.goods.feign"})
public class ItemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItemApplication.class);
    }
}
