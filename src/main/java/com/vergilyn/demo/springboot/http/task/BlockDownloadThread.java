package com.vergilyn.demo.springboot.http.task;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import com.vergilyn.demo.springboot.http.bean.BlockFileBean;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/25
 */
public class BlockDownloadThread implements Runnable {

    private BlockFileBean block;
    // 临时目录
    private String tempPath;

    /**
     * @param block 下载任务信息
     * @param tempPath 临时文件目录
     */
    public BlockDownloadThread(BlockFileBean block, String tempPath) {
        this.block = block;
        this.tempPath = tempPath;
    }

    @Override
    public void run() {
        Date begin = new Date();
        InputStream is = null;
        OutputStream os = null;
        try {
            // 执行下载
            URLConnection conn = new URL(block.getDownloadUrl()).openConnection();
            // -1: 因为bytes=0-499, 表示contentLength=500.
            conn.setRequestProperty(HttpHeaders.RANGE, "bytes=" + block.getBeginOffset() + "-" + (block.getEndOffset() - 1));
            conn.setDoOutput(true);

            is = conn.getInputStream();
            File file = new File(tempPath + File.separator + block.getBlockFileName());
            os = new FileOutputStream(file);

            // FIXME: 对下载(对文件操作)并没有太多了解, 所以不知道具体那种"下载"的写法会更好.
            IOUtils.copy(is, os);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(block.getBlockFileName() + " download error: " + e.getMessage());
            return; // 注意要return
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        Date end = new Date();
        // 简单计算下载速度, 我把连接时间也算在内了
        long speed = (block.getEndOffset() - block.getBeginOffset()) / 1024L
                / (end.getTime() - begin.getTime()) * 1000L;
        System.out.println(block.getBlockFileName() + " aver-speed: " + speed + " kb/s");

        // FIXME: 实际中需要更新表BlockFileBean的信息, 标记分块已下载完成, 记录平均下载速度、下载完成时间等需要的信息
        // (省略)更新表BlockFileBean
    }
}
