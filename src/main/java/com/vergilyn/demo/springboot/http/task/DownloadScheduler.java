package com.vergilyn.demo.springboot.http.task;

import java.io.File;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSON;
import com.vergilyn.demo.springboot.http.bean.BlockFileBean;
import com.vergilyn.demo.springboot.http.thread.BlockDownloadThread;
import com.vergilyn.demo.springboot.http.util.ConstantUtils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 模拟实际应用中的定时器任务, 定时检索需要执行下载的任务.
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/25
 */
@Component
public class DownloadScheduler {

    @Autowired
    StringRedisTemplate redisTemplate;

    public void execDownloadTask(){
        // 检索需要执行下载的文件块, 实际中更可能是用sql查询表"BlockFileBean"得到
        List<String> tasks = redisTemplate.opsForList().range(ConstantUtils.keyDownloadTask(), 0, -1);

        if(tasks == null || tasks.isEmpty()){
            return;
        }

        // 多线程下载, 实际中更多可能用spring的线程池
        // 我对线程池也没太多了解, 实际中我只特别注意了ArrayBlockingQueue、CallerRunsPolicy.
        // 实际中我把任务等待队列设置成一定比总任务数大
        // (因为实际中我每天只执行一次下载定时任务, 每次只下载700个小块, 所以ArrayBlockingQueue我设置的800. 并且我没有保留核心线程)
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2,
                6,
                30,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<Runnable>(100),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.allowCoreThreadTimeOut(true);

        for (String blockKey : tasks) {
            List<String> blocks = redisTemplate.opsForList().range(blockKey, 0, -1);
            if(blocks == null || blocks.isEmpty()){
                continue;
            }
            // 创建临时目录
            String tempPath = ConstantUtils.TEMP_PATH + File.separator + StringUtils.substringAfter(blockKey, ":");
            File dir = new File(tempPath);
            if(!dir.exists()){
                boolean mkdirs = dir.mkdirs();
            }


            for (String block : blocks) {
                BlockFileBean blockFile = JSON.parseObject(block, BlockFileBean.class);
                executor.execute(new BlockDownloadThread(blockFile, tempPath));
            }

            // 只是为了测试写的代码逻辑
            redisTemplate.opsForList().rightPush(ConstantUtils.keyMergeTask(), tempPath);
        }
        executor.shutdown(); // 阻止新任务提交, 已有任务会执行完.

    }

}
