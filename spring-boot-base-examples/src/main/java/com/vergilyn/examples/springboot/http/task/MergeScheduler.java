package com.vergilyn.examples.springboot.http.task;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.vergilyn.examples.springboot.http.util.ConstantUtils;
import com.vergilyn.examples.springboot.http.util.FileMergeUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 合并已下载完成文件
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/25
 */
@Component
public class MergeScheduler {

    @Autowired
    StringRedisTemplate redisTemplate;

    /**
     * 合并文件,
     */
    public void mergeBlock() {
        // FIXME: 找到所有需要合并文件的任务
        List<String> mergeTasks = redisTemplate.opsForList().range(ConstantUtils.keyMergeTask(), 0, -1);
        if(mergeTasks == null || mergeTasks.isEmpty()){
            return;
        }

        for (String tempPath : mergeTasks) {
            Date begin = new Date();
            File dir = new File(tempPath);
            LinkedList<File> files = (LinkedList<File>) FileUtils.listFiles(dir, null, false);

            // "块"文件排序
            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    String index1 = StringUtils.substringAfterLast(o1.getName(), ".");
                    String index2 = StringUtils.substringAfterLast(o2.getName(), ".");
                    return new Integer(index1).compareTo(new Integer(index2));
                }
            });

            // FIXME 此处demo写的比较死, 更好的是传CompeteFileId过来, 然后得到块目录和文件名称(完整路径)
            String dest = ConstantUtils.TEMP_PATH + StringUtils.substringAfter(tempPath, "\\") + ".exe";

            FileMergeUtil.randomAccessFile(dest, files, 8 * 1024);

            Date end = new Date();

            System.out.println("merge time consume: " + (end.getTime() - begin.getTime()) + " ms");

            try {
                // FIXME 维护表CompleteFileBean

                // 删除不需要的临时文件; 以下代码不一定能成功删除, 具体调试(可能是有stream未关闭, 可能删除方法有问题)
                FileUtils.deleteDirectory(dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        try {
            FileUtils.deleteDirectory(new File("D:\\temp\\53245787-d1e1-455e-825e-43be50b447c3"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
