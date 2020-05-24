package com.changgou.dao;

import com.changgou.goods.pojo.Brand;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * DAO 使用通用Mapper
 * DSO接口需要继承 tk.mybatis.mapper.common.Mapper
 */
@Repository
public interface BrandMapper extends Mapper<Brand> {


}
