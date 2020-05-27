package com.changgou.test;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * 数据库测试类
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbcTest {

    @Autowired
    private BrandMapper brandMapper;

    @Test
    public void jdbcTest() {
        List<Brand> brands = brandMapper.selectAll();
        for (Brand brand : brands) {
            log.info(brand.toString());
        }
    }
}
