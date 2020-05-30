package com.changgou.canal;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.InsertListenPoint;
import lombok.extern.slf4j.Slf4j;

/**
 * 实现mysql数据监听
 */
@CanalEventListener
@Slf4j
public class CanalDataEventListener {

    /**
     * @InsertListenPoint 增加监听
     * @param eventType 当前操作的类型： 增加数据
     * @param rowData 发生变更的一行数据
     */
    @InsertListenPoint
    public void onEventInsert(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {

        for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
            log.info("列名：" + column.getName() + "------ 变更的数据：" + column.getValue());
        }
    }
}
