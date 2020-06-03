package com.changgou;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @File: com.changgou.SearchApplication
 * @Description: 搜索微服务启动类
 * @Author: tom
 * @Create: 2020-06-03 17:40
 **/

// 使用 feign 查询，故排斥禁用数据库加载
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableEurekaClient
public class SearchApplication {

    public static void main(String[] args) {

        /**
         * Springboot整合es在项目启动前设置，防止报错
         * 解决 netty 冲突后初始化 client 时还会抛出异常
         * availableProcesseors is already set to [12], reject[12]
         */
//        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(SearchApplication.class);
    }
}
