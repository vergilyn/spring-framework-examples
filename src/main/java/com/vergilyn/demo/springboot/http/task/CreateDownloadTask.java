package com.vergilyn.demo.springboot.http.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.vergilyn.demo.springboot.http.bean.BlockFileBean;
import com.vergilyn.demo.springboot.http.bean.CompleteFileBean;
import com.vergilyn.demo.springboot.http.util.FileMergeUtil;
import com.vergilyn.demo.springboot.http.util.RedisUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
@Component
public class CreateDownloadTask {
    /** 每个小文件大小: 10MB*/
    public static final long UNIT_SIZE = 10 * 1024 * 1024;
    public static final String TEMP_PATH = "D://temp//";


    @Qualifier("stringRedisTemplate")
    @Autowired
    StringRedisTemplate redisTemplate;

    public String createTask(String url){
        if(StringUtils.isBlank(url)){
            return "";
        }

        // 1. 获取远程文件大小、下载文件名
        long contentLength = 0;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            // FIXME 并未严格校验这是一个下载URL
            if(conn.getResponseCode() == HttpStatus.OK.value()){
                contentLength = conn.getContentLength();
                // conn.getHeaderField(HttpHeaders.CONTENT_DISPOSITION);  // 可能从Content-Disposition中得到资源名称
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(conn != null){
                try {
                    conn.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if(contentLength <= 0){
            throw new RuntimeException("获取远程文件大小失败!");
        }

        // remark: 实际中需要具体分析url判断如何获取资源名称
        String fileName = StringUtils.substringAfterLast(url, "/");

        CompleteFileBean fileBean = new CompleteFileBean(url, fileName, contentLength);
        String id = fileBean.getId();

        // (省略)实际可能会保存fileBean的记录到数据库

        // 2. 拆分大文件
        String key = RedisUtils.keyFile(id);
        BlockFileBean block;
        if(contentLength <= UNIT_SIZE){
            block = new BlockFileBean(id, getBlockName(fileName, 1), url, 0, contentLength );
            redisTemplate.opsForList().rightPushAll(key, JSON.toJSONString(block));
        }else{
            List<String> blocks = new ArrayList<>();
            long begin = 0;
            int index = 1;
            while(begin < contentLength){
                long end = begin + UNIT_SIZE <= contentLength ? begin + UNIT_SIZE : contentLength;
                block = new BlockFileBean(id, getBlockName(fileName, index++), url, begin, end );
                blocks.add(JSON.toJSONString(block));
                begin += UNIT_SIZE;
            }
            redisTemplate.opsForList().rightPushAll(key, blocks);
        }
        return key;
    }

    private static String getBlockName(String fileName, int index){
        return fileName + "." + String.format("%03d", index);
    }


    /**
     * 执行下载任务
     * @param taskKey
     * @return 块文件目录
     */
    public String execDownloadTask(String taskKey){
        List<String> range = redisTemplate.opsForList().range(taskKey, 0, -1);

        // 创建临时目录
        String path = TEMP_PATH + File.separator + StringUtils.substringAfter(taskKey, ":");
        File dir = new File(path);
        if(!dir.exists()){
            boolean mkdirs = dir.mkdirs();
        }

        InputStream is = null;
        OutputStream os = null;
        for (String s : range) {
            BlockFileBean block = JSON.parseObject(s, BlockFileBean.class);
            Date begin = new Date();
            try {
                // 执行下载
                // FIXME 实际中, 建议开多个线程下载, 避免某块下载过慢导致其余块下载任务阻塞
                URLConnection conn = new URL(block.getDownloadUrl()).openConnection();
                // -1: 因为bytes=0-499, 表示contentLength=500.
                conn.setRequestProperty(HttpHeaders.RANGE, "bytes=" + block.getBeginOffset() + "-" + (block.getEndOffset() - 1));
                conn.setDoOutput(true);

                is = conn.getInputStream();
                File file = new File(path + File.separator + block.getBlockFileName());
                os = new FileOutputStream(file);

                IOUtils.copy(is, os);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(os);
            }
            Date end = new Date();
            // 简单计算下载速度, 我把连接时间也算在内了
            long speed = (block.getEndOffset() - block.getBeginOffset()) / 1024L
                    / (end.getTime() - begin.getTime()) * 1000L;
            System.out.println(block.getBlockFileName() + " aver-speed: " + speed + " kb/s");
        }

        return path;
    }

    public void mergeBlock(String path) {
        Date begin = new Date();
        File dir = new File(path);
        File[] files = dir.listFiles();

        // FIXME 此处demo写的比较死, 更好的是传CompeteFileId过来, 然后得到块目录和文件名称(完整路径)
        String dest = TEMP_PATH + StringUtils.substringAfter(path, "\\") + ".exe";

        FileMergeUtil.randomAccessFile(dest, files, 8 * 1024);

        Date end = new Date();

        System.out.println("merge time consume: " + (end.getTime() - begin.getTime()) + " ms");

        // 合并成功, 删除块文件.
        // FIXME下面代码不一定能删除, 具体调试
        try {
            FileUtils.forceDeleteOnExit(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
