package com.changgou.common.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @File: JwtTest
 * @Description: 令牌生成和解析测试
 * @Author: tom
 * @Create: 2020-06-11 09:33
 **/
@Slf4j
public class JwtTest {

    private String signKey = "tomTest";

    /**
     * 创建令牌
     */
    @Test
    public void testCreateToken() throws InterruptedException {
        // 构建jwt令牌对象
        JwtBuilder builder = Jwts.builder();
        builder.setIssuer("tom"); // 颁发者
        builder.setIssuedAt(new Date()); // 颁发日期
        builder.setExpiration(new Date(System.currentTimeMillis() + 20000)); // 设置定时，2秒过期
        builder.setSubject("jwt 令牌测试"); // 主题测试
        builder.signWith(SignatureAlgorithm.HS256, signKey); // 1. 签名算法 2. 秘钥
        // 自定义载荷信息
        Map<String, Object> userInfo = new HashMap<String,Object>();
        userInfo.put("company", "dis");
        userInfo.put("address", "北京");
        userInfo.put("money", 3500);
        // 添加载荷
        builder.addClaims(userInfo);

        // 获取生成 token
        String token = builder.compact();
        System.out.println(token);

//        Thread.sleep(3000);
        parseToken(token);
    }

    /**
     * 令牌解析
     */
    public void parseToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(signKey).parseClaimsJws(token).getBody();
        System.out.println(claims.toString());
    }
}
