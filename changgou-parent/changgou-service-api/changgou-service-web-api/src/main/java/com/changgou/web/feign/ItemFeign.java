package com.changgou.web.feign;

import com.changgou.common.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @File: ItemFeign
 * @Description:
 * @Author: tom
 * @Create: 2020-06-09 17:15
 **/
@FeignClient(name = "page")
@RequestMapping("/item")
public interface ItemFeign {

    /**
     * 根据SpuID生成静态页面
     * @param spuId
     * @return
     */
    @GetMapping("/createHtml/{id}")
    @ResponseBody
    Result createHtml(@PathVariable("id") Long spuId);
}
