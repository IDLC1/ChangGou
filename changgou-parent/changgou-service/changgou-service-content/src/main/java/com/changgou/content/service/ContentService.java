package com.changgou.content.service;

import com.changgou.content.pojo.Content;

import java.util.List;

/**
 * @File: ContentService
 * @Description: 广告内容的服务类
 * @Author: tom
 * @Create: 2020-05-30 11:08
 **/
public interface ContentService {

    /**
     * 根据分类id查询广告内容
     * @param id
     * @return
     */
    public List<Content> findByCategory(Long id);
}
