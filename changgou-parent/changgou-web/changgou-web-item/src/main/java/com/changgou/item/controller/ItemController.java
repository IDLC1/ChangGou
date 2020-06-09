package com.changgou.item.controller;

import com.changgou.common.entity.Page;
import com.changgou.common.entity.Result;
import com.changgou.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @File: PageController
 * @Description:
 * @Author: tom
 * @Create: 2020-06-09 15:38
 **/
@Controller
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService pageService;

    @GetMapping("/createHtml/{id}")
    @ResponseBody
    public Result createHtml(@PathVariable("id") Long spuId) {
        pageService.buildPage(spuId);
        return Result.ok("创建成功");
    }

    /**
     * 查看具体的商品页面
     * @return
     */
    @GetMapping("/page")
    public String search(Model model)  {
        return "item";
    }
}
