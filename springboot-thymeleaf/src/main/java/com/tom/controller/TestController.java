package com.tom.controller;

import com.tom.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

/**
 * Thymeleaf 模板引擎
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("message", "hello thymeleaf");

        List<User> lists = new ArrayList<User>();
        lists.add(new User(1, "小王", "深圳"));
        lists.add(new User(2, "小美", "湖南"));
        lists.add(new User(3, "小李222", "东京"));
        model.addAttribute("users", lists);

        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("num", "105");
        dataMap.put("name", "小王");
        dataMap.put("address", "深圳");
        model.addAttribute("dataMap", dataMap);

        model.addAttribute("now", new Date());

        model.addAttribute("age", 22);
        return "demo1";
    }

}
