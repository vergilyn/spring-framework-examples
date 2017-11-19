package com.vergilyn.demo.springboot.http.util;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/19
 */
public class RedisUtils {

    public static String keyFile(String id){
        return "complete_file:"+id;
    }

    public static String keyBlock(String id){
        return "block_file:"+id;
    }
}
