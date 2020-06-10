package com.changgou.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * Base64 加解密封装
 */
@Slf4j
public class Base64Util {

    /**
     * base64 加密
     * @param sourceStr 明文字符串
     * @return
     */
    public static String enCode(String sourceStr) {
        byte[] encode = Base64.getEncoder().encode(sourceStr.getBytes());
        String encodeStr = null;
        try {
            encodeStr = new String(encode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("加密失败");
        }
        return encodeStr;
    }

    /**
     * base64 解密
     * @param enStr 被加密的字符串
     * @return
     */
    public static String deCode(String enStr) {
        byte[] decode = Base64.getDecoder().decode(enStr);
        String str = null;
        try {
            str = new String(decode, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.info("解密失败");
        }
        return str;
    }
}
