//package com.changgou.item.listener;
//
//import com.changgou.item.service.ItemService;
//import com.changgou.web.config.RabbitMQConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @File: PageListener
// * @Description:
// * @Author: tom
// * @Create: 2020-06-09 15:04
// **/
//@Component
//@Slf4j
//public class ItemListener {
//
//    @Autowired
//    private ItemService itemService;
//
//    @RabbitListener(queues = RabbitMQConfig.PAGE_CREATE_QUEUE)
//    public void receiveMessage(Long spuId) {
//        log.info("生成商品详情页面，商品id为：" + spuId);
//        // 生成静态化页面
//        itemService.buildPage(spuId);
//    }
//}
