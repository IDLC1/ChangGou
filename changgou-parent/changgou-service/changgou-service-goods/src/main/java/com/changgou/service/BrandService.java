package com.changgou.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface BrandService {

    /**
     * 查询所有品牌信息
     * @return
     */
    List<Brand> findAll();

    /**
     * 多条件搜索
     * @param brand
     * @return
     */
    List<Brand> findList(Brand brand);

    Brand findById(Integer id);

    /**
     * 分页获取
     * @param page  当前页
     * @param size  每页显示的条数
     * @return
     */
    PageInfo<Brand> pageSearch(Integer page, Integer size);

    /**
     * 分页 + 条件搜索
     * @param brand
     * @param page
     * @param size
     * @return
     */
    PageInfo<Brand> pageSearchAndCondition(Brand brand, Integer page, Integer size);

    void add(Brand brand);

    void update(Brand brand);

    void del(Integer id);
}
