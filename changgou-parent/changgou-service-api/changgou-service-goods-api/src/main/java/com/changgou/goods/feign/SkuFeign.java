package com.changgou.goods.feign;

import com.changgou.common.entity.Result;
import com.changgou.goods.pojo.Sku;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @File: SkuFeign
 * @Description: 提供SKU操作
 * @Author: tom
 * @Create: 2020-06-04 09:08
 **/
@FeignClient(name = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    @GetMapping
    Result<List<Sku>> findAll();

    @GetMapping("/list/{spuId}")
    Result<List<Sku>> findSkuListBySpuId(@PathVariable("spuId") Long spuId);
}
