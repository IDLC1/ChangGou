package com.changgou.controller;

import com.changgou.common.entity.Result;
import com.changgou.common.entity.StatusCode;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageInfo;
import jdk.net.SocketFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brand")
// 解决跨域问题
@CrossOrigin
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 查询所有品牌
     */
    @GetMapping
    public Result<List<Brand>> findAll() {
        List<Brand> brands = brandService.findAll();
        return new Result<>(true, StatusCode.OK, "查询品牌成功", brands);
    }

    @GetMapping("/{id}")
    public Result<Brand> findById(@PathVariable(value = "id") Integer id) {
        Brand brand = brandService.findById(id);
        return new Result<Brand>(true, StatusCode.OK, "查找成功", brand);
    }

    /**
     * 条件查询
     */
    @PostMapping(value = "/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand) {
        List<Brand> list = brandService.findList(brand);
        return new Result<List<Brand>>(true, StatusCode.OK, "条件搜索成功", list);
    }

    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable(value = "page")Integer page, @PathVariable(value = "size") Integer size) {
        PageInfo<Brand> pageInfo = brandService.pageSearch(page, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "搜索分页成功", pageInfo);
    }

    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,@PathVariable(value = "page")Integer page, @PathVariable(value = "size") Integer size) {
        PageInfo<Brand> pageInfo = brandService.pageSearchAndCondition(brand, page, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK, "搜索分页成功", pageInfo);
    }

    /**
     * 增加品牌
     * @param brand
     */
    @PostMapping()
    public Result add(Brand brand) {
        // 调用service实现操作
        brandService.add(brand);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 品牌修改
     */
    @PutMapping(value = "/{id}")
    public Result update(@PathVariable(value = "id")Integer id, @RequestBody Brand brand) {
        brand.setId(id);
        brandService.update(brand);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    /**
     * 品牌删除
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable(value = "id") Integer id) {
        brandService.del(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
}
