package com.changgou.search.service;

import java.sql.SQLException;
import java.util.Map;

/**
 * @File: SkuService
 * @Description: SKU服务
 * @Author: tom
 * @Create: 2020-06-04 09:12
 **/
public interface SkuService {

    /**
     * 条件搜索
     */
    Map<String, Object> search(Map<String, String> searchMap);

    /**
     * 导入索引库
     */
    void importData();
}
