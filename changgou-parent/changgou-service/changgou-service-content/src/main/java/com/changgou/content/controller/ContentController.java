package com.changgou.content.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.content.pojo.Content;
import com.changgou.content.service.ContentService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @File: ContentController
 * @Description: 广告获取的控制类
 * @Author: tom
 * @Create: 2020-05-30 11:15
 **/

@Api(value = "ContentController")
@RestController
@RequestMapping("/content")
@CrossOrigin
@Slf4j
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/list/category/{id}")
    public Result<List<Content>> findByCategory(@PathVariable  Long id) {
        log.info("Content微服务 根据类别ID查询信息内容列表");
        // 根据分类ID查询广告集合
        List<Content> list = contentService.findByCategory(id);
        return new Result<List<Content>>(true, StatusCode.OK, "查询成功", list);
    }
}
