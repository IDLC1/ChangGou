package com.changgou.canal;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.changgou.common.entity.Result;
import com.changgou.content.feign.ContentFeign;
import com.changgou.content.pojo.Content;
import com.xpand.starter.canal.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现mysql数据监听
 */
@CanalEventListener
@Slf4j
public class CanalDataEventListener {

    @Autowired
    private ContentFeign contentFeign;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * @ListenPoint 自定义监听
     * @param eventType 当前操作的类型： 增加数据
     * @param rowData 发生变更的一行数据
     */
    @ListenPoint(
            destination = "example", // 指定Canal实例的地址
            schema = {"changgou_content"}, // 指定监听的数据库
            table = {"tb_content", "tb_content_category"}, // 指定监控的表
            eventType = {
                    CanalEntry.EventType.DELETE,
                    CanalEntry.EventType.UPDATE,
                    CanalEntry.EventType.INSERT} // 监听类型
    )
    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        log.info("Canal 微服务 监控到了数据变化~");
        // 1. 获取列名为 category_id 的值
        String categoryId = getColumnValue(eventType, rowData);

        ///////// 后面的部分为监控到数据后的处理 //////////
        // 2. 调用feign获取该分类下的所有广告集合，也可以控制其他微服务作出相应变化
        Result<List<Content>> categoryRes = contentFeign.findByCategory(Long.valueOf(categoryId));
        List<Content> data = categoryRes.getData();

        // 3. 使用redisTemplate存储到redis中
        stringRedisTemplate.boundValueOps("content_" + categoryId).set(JSON.toJSONString(data));
        log.info("Canal 微服务 更新了redis服务~");
    }

    /**
     * 获取变动的数据的分类id
     * @param eventType
     * @param rowData
     * @return
     */
    private String getColumnValue(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
        String categoryId = "";

        List<CanalEntry.Column> colList;
        if (eventType == CanalEntry.EventType.DELETE) {
            colList = rowData.getBeforeColumnsList();
        } else {
            colList = rowData.getAfterColumnsList();
        }
        for (CanalEntry.Column column : colList) {
            log.info("自定义操作： 列名：" + column.getName() + "------ 变更的数据：" + column.getValue());

            if (column.getName().equalsIgnoreCase("category_id")) {
                categoryId = column.getValue();
                return categoryId;
            }
        }
        return categoryId;
    }
//    /**
//     * @ListenPoint 自定义监听
//     * @param eventType 当前操作的类型： 增加数据
//     * @param rowData 发生变更的一行数据
//     */
//    @ListenPoint(
//            destination = "example", // 指定Canal实例的地址
//            schema = {"changgou_content"}, // 指定监听的数据库
//            table = {"tb_content", "tb_content_category"}, // 指定监控的表
//            eventType = {
//                    CanalEntry.EventType.DELETE,
//                    CanalEntry.EventType.UPDATE,
//                    CanalEntry.EventType.INSERT} // 监听类型
//    )
//    public void onEventCustomUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            log.info("自定义操作前： 列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
//        }
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            log.info("自定义操作后： 列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
//        }
//    }
//
//    /**
//     * @InsertListenPoint 增加监听
//     * @param eventType 当前操作的类型： 增加数据
//     * @param rowData 发生变更的一行数据
//     */
//    @InsertListenPoint
//    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            log.info("增加： 列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
//        }
//    }
//
//    /**
//     * @UpdateListenPoint 修改监听
//     * @param eventType 当前操作的类型： 增加数据
//     * @param rowData 发生变更的一行数据
//     */
//    @UpdateListenPoint
//    public void onEventUpdate(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        log.info("===================================================================================");
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            log.info("修改前： 列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
//        }
//        log.info("===================================================================================");
//        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
//            log.info("修改后： 列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
//        }
//    }
//
//    /**
//     * @DeleteListenPoint 删除监听
//     * @param eventType 当前操作的类型： 增加数据
//     * @param rowData 发生变更的一行数据
//     */
//    @DeleteListenPoint
//    public void onEventDel(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
//        for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
//            log.info("删除前：列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
//        }
//    }

}
