package com.vergilyn.examples.springboot.http.util;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/19
 */
public class ConstantUtils {
    /** 每个小文件大小: 10MB*/
    public static final long UNIT_SIZE = 10 * 1024 * 1024;
    public static final String TEMP_PATH = "D://temp//";
    /** 是否限制下载速度 */
    public static final boolean IS_LIMIT_SPEED = true;
    /** 下载限速, 单位: kb/s */
    public static final long DOWNLOAD_SPEED = 400;

    public static String keyFile(String id){
        return "complete_file:"+id;
    }

    public static String keyBlock(String id){
        return "block_file:"+id;
    }

    public static String keyDownloadTask(){
        return "block_download_task";
    }

    public static String keyMergeTask(){
        return "merge_file_task";
    }
}
