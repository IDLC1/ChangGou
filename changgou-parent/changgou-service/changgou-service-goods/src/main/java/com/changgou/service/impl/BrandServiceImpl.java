package com.changgou.service.impl;

import com.changgou.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private BrandMapper brandMapper;

    public Example createExample(Brand brand) {
        // 自定义条件搜索对象 Example
        Example example = new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria(); //条件构造器

        if (brand != null) {
            if (!StringUtils.isEmpty(brand.getName())) {
                criteria.andLike("name", '%' + brand.getName() + '%');
            }

            if (!StringUtils.isEmpty(brand.getLetter())) {
                criteria.andEqualTo("letter", brand.getLetter());
            }
        }
        return example;
    }

    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }

    @Override
    public List<Brand> findList(Brand brand) {
        Example example = createExample(brand);
        return brandMapper.selectByExample(example);
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 分页查询
     * @param page  当前页
     * @param size  每页显示的条数
     * @return
     */
    @Override
    public PageInfo<Brand> pageSearch(Integer page, Integer size) {
        // 分页实现
        // 后面的查询必须是紧跟集合查询
        PageHelper.startPage(page, size);
        // 查询集合
        List<Brand> brands = brandMapper.selectAll();
        return new PageInfo<Brand>(brands);
    }

    @Override
    public PageInfo<Brand> pageSearchAndCondition(Brand brand, Integer page, Integer size) {
        // 开始分页
        PageHelper.startPage(page, size);
        // 搜索数据
        Example example = createExample(brand);
        List<Brand> list = brandMapper.selectByExample(example);
        return new PageInfo<Brand>(list);
    }

    /**
     * 增加品牌
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        // 使用通用 Mapper.insertSelective
        // 方法中但凡带有selective就会忽略空值
        int i = brandMapper.insertSelective(brand);
    }

    /**
     * 根据id修改品牌
     * @param brand
     */
    @Override
    public void update(Brand brand) {
        // 使用通用mapper.update();
        brandMapper.updateByPrimaryKeySelective(brand);
    }

    /**
     * 根据id删除
     * @param id
     */
    @Override
    public void del(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }
}
