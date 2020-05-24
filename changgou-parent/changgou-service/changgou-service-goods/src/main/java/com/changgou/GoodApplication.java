package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableEurekaClient // 开启eureka客户端
// 开启通用Mapper的包扫描
@MapperScan(basePackages = {"com.changgou.dao"})
public class GoodApplication {
    public static void main(String[] args) {
        SpringApplication.run(GoodApplication.class);
    }
}