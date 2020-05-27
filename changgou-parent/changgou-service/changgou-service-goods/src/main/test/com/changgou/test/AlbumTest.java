package com.changgou.test;

import com.changgou.controller.AlbumController;
import com.changgou.goods.pojo.Album;
import com.changgou.service.AlbumService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @File: AlbumTest
 * @Description: 相册测试类
 * @Author: tom
 * @Create: 2020-05-27 15:08
 **/
public class AlbumTest {

    @Autowired
    private AlbumController albumController;

    @Test
    public void findTest() {
        List<Album> all = albumController.findAll();
    }
}
