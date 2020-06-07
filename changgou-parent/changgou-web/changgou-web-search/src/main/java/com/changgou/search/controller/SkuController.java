package com.changgou.search.controller;

import com.changgou.common.entity.Page;
import com.changgou.search.feign.SkuFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.changgou.search.pojo.SkuInfo;

import java.util.Map;

@Controller
@RequestMapping("/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;

    /**
     * 实现搜索调用
     * @return
     */
    @GetMapping("/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model)  {
        // 调用搜索服务
        Map<String, Object> map = skuFeign.search(searchMap);
        model.addAttribute("result", map);

        // 计算分页
        Page<SkuInfo> pageInfo = new Page<SkuInfo>(
                Long.parseLong(map.get("total").toString()),
                Integer.parseInt(map.get("pageNumber").toString())+1,
                Integer.parseInt(map.get("pageSize").toString())
        );
        model.addAttribute("pageInfo", pageInfo);

        // 将条件存储用于页面回显数据
        model.addAttribute("searchMap", searchMap);

        String[] urls = url(searchMap);
        model.addAttribute("url", urls[0]);
        model.addAttribute("sortUrl", urls[1]);
        return "search";
    }


    /**
     * 拼接组装用户请求的url地址
     * 获取用户每次请求的地址
     * 页面需要在这次请求的地址上面添加额外的搜索条件
     * @param searchMap
     * @return
     */
    public String[] url(Map<String, String> searchMap) {
        String url = "/search/list";
        String sortUrl = "/search/list";
        if (searchMap != null && searchMap.size() > 0) {
            url += '?';
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();

                // 跳过分页参数
                if (key.equalsIgnoreCase("pageNum")) {
                    continue;
                }

                String value = entry.getValue();
                url += key + "=" + value + "&";

                if (key.equalsIgnoreCase("sortField") || key.equalsIgnoreCase("sortRule")) {
                    continue;
                }
                sortUrl = key + "=" + value + "&";
            }

            // 去掉最末尾的&
            url = url.substring(0, url.length() - 1);
            sortUrl = sortUrl.substring(0, sortUrl.length() - 1);
        }

        return new String[]{url, sortUrl};
    }
}
