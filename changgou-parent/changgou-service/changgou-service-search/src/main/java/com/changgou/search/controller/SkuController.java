package com.changgou.search.controller;

import com.changgou.common.entity.Result;
import com.changgou.search.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @File: SkuController
 * @Description: Sku控制层
 * @Author: tom
 * @Create: 2020-06-04 09:25
 **/
@RestController
@CrossOrigin
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuService skuService;

    @GetMapping("/import")
    public Result importData() {
        skuService.importData();
        return Result.ok("导入数据到索引库中成功");
    }

    /**
     * 搜索实现
     */
    @GetMapping
    public Map search(@RequestParam(required = false) Map<String, String> searchMap){
        return skuService.search(searchMap);
    }
}
