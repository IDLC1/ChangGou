package com.changgou.content.feign;

import com.changgou.common.entity.Result;
import com.changgou.content.pojo.Content;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @File: ContentFeign
 * @Description: 广告Feign
 * @Author: tom
 * @Create: 2020-05-30 14:02
 **/

@FeignClient(name="content")
@RequestMapping("/content")
public interface ContentFeign {

    /**
     * 根据分类ID查询所有广告
     * @param id
     * @return
     */
    @GetMapping("/list/category/{id}")
    Result<List<Content>> findByCategory(@PathVariable Long id);
}
