package com.changgou.common.base64;

import com.changgou.common.utils.Base64Util;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.util.Base64Utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@Slf4j
public class Base64Test {



    /**
     * base64 加密测试
     */
    @Test
    public void test() {
        String enStr = Base64Util.enCode("abcdefg");
        log.info("加密后数据：" + enStr);

        String deStr = Base64Util.deCode(enStr);
        log.info("解密后数据：" + deStr);
    }

}
