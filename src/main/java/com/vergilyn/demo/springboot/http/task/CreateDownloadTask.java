package com.vergilyn.demo.springboot.http.task;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.vergilyn.demo.springboot.http.bean.BlockFileBean;
import com.vergilyn.demo.springboot.http.bean.CompleteFileBean;
import com.vergilyn.demo.springboot.http.util.ConstantUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
@Component
public class CreateDownloadTask {
    @Autowired
    StringRedisTemplate redisTemplate;

    public void createTask(String url){
        if(StringUtils.isBlank(url)){
            return ;
        }

        // 1. 获取远程文件大小、下载文件名
        long contentLength = 0;
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            // FIXME 并未严格校验这是一个下载URL
            if(conn.getResponseCode() == HttpStatus.OK.value()){
                contentLength = conn.getContentLength();
                // conn.getHeaderField(HttpHeaders.CONTENT_DISPOSITION);  // 资源名称又可能要从Content-Disposition中得到
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

        // FIXME: 实际中需要具体分析url判断如何获取资源名称
        String fileName = StringUtils.substringAfterLast(url, "/");

        // FIXME: 校验下载任务是否存在, 存在则不需要再次创建
        // 比如: 表CompleteFileBean有存在这个download-url的数据, 且contentLength一致, 则不在执行下面的逻辑.

        CompleteFileBean fileBean = new CompleteFileBean(url, fileName, contentLength);
        String id = fileBean.getId();
        // 模拟保存到数据库
        redisTemplate.opsForValue().set(ConstantUtils.keyFile(id), JSON.toJSONString(fileBean));

        // 2. 分割大文件
        createSplitFile(fileBean);
    }

    /**
     * 分割文件
     * @param fileBean 完整文件信息
     */
    private void createSplitFile(CompleteFileBean fileBean){
        String key = ConstantUtils.keyBlock(fileBean.getId());
        String fileId = fileBean.getId();
        String fileName = fileBean.getFileName();
        String url = fileBean.getDownloadUrl();
        long contentLength = fileBean.getContentLength();

        BlockFileBean block;
        List<String> blocks = new ArrayList<>();

        if(contentLength <= ConstantUtils.UNIT_SIZE){
            block = new BlockFileBean(fileId, getBlockName(fileName, 1), url, 0, contentLength );
            blocks.add(JSON.toJSONString(block));
        }else{
            long begin = 0;
            int index = 1;
            while(begin < contentLength){
                long end = begin + ConstantUtils.UNIT_SIZE <= contentLength ? begin + ConstantUtils.UNIT_SIZE : contentLength;
                block = new BlockFileBean(fileId, getBlockName(fileName, index++), url, begin, end );
                blocks.add(JSON.toJSONString(block));
                begin += ConstantUtils.UNIT_SIZE;
            }
        }

        if(blocks.size() > 0){
            // 模拟保存数据库: 生成每个小块的下载任务, 待定时器读取任务下载
            redisTemplate.opsForList().rightPushAll(key, blocks);
            // 保存需要执行下载的任务, 实际应用中是通过sql得到.
            redisTemplate.opsForList().rightPushAll(ConstantUtils.keyDownloadTask(), key);
        }
    }

    private static String getBlockName(String fileName, int index){
        return fileName + "." + String.format("%03d", index);
    }

}
