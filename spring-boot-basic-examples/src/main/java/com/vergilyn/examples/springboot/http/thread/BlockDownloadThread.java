package com.vergilyn.examples.springboot.http.thread;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

import com.vergilyn.examples.springboot.http.bean.BlockFileBean;
import com.vergilyn.examples.springboot.http.util.ConstantUtils;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.http.HttpHeaders;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/25
 */
public class BlockDownloadThread implements Runnable {

    private final BlockFileBean block;
    // 临时目录
    private final String tempPath;

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
        byte[] buffer = new byte[1024]; // 缓冲区大小
        long totalSize = block.getEndOffset() - block.getBeginOffset();
        long begin = System.currentTimeMillis();
        InputStream is = null;
        RandomAccessFile os = null;
        try {
            // FIXME: 对下载(对文件操作)并没有太多了解, 所以不知道具体那种"下载"的写法会更好, 但看到很多都是RandomAccessFile实现的.
            URLConnection conn = new URL(block.getDownloadUrl()).openConnection();
            // -1: 因为bytes=0-499, 表示contentLength=500.
            conn.setRequestProperty(HttpHeaders.RANGE, "bytes=" + block.getBeginOffset() + "-" + (block.getEndOffset() - 1));
            conn.setDoOutput(true);

            is = conn.getInputStream();

            File file = new File(tempPath + File.separator + block.getBlockFileName());
            os = new RandomAccessFile(file, "rw");

            int len;
            // 是否限制下载速度
            if(ConstantUtils.IS_LIMIT_SPEED){ // 限制下载速度

                /* 思路:
                 *  假设下载速度上限是m(kb/s), 发送n个字节的理论耗时: n / 1024 / m; 然而实际耗时 t(s), 那么则需要休眠 n / 1024 / m - t;
                 */
                // 需要注意: System.currentTimeMillis(), 可能多次得到的时间相同, 详见其API说明.
                SpeedLimit sl = new SpeedLimit(ConstantUtils.DOWNLOAD_SPEED, System.currentTimeMillis());

                while((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);

                    sl.updateWrite(len);
                    sl.updateEndTime(System.currentTimeMillis());

                    long timeConsuming = sl.getEndTime() - sl.getBeginTime() + 1; // +1: 避免0

                    // 当前平均下载速度: kb/s, 实际中可以直接把 b/ms 约等于 kb/ms (减少单位转换逻辑)
                    double currSpeed = sl.getWriteSize() / 1024D / timeConsuming * 1000D;
                    if(currSpeed > sl.getSpeed()){ // 当前下载速度超过限制速度
                        // 休眠时长 = 理论限速时常 - 实耗时常;
                        double sleep = sl.getWriteSize() / 1024D / sl.getSpeed() * 1000D - timeConsuming;
                        if(sleep > 0){
                            try {
                                Thread.sleep((long) sleep);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }else{
                while((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }
            }

            os.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(block.getBlockFileName() + " download error: " + e.getMessage());
            return; // 注意要return
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }
        long end = System.currentTimeMillis() ;
        // 简单计算下载速度, 我把连接时间也算在内了
        double speed = totalSize / 1024D / (end - begin + 1) * 1000D; // +1: 避免0
        System.out.println(block.getBlockFileName() + " aver-speed: " + speed + " kb/s");

        // FIXME: 实际中需要更新表BlockFileBean的信息, 标记分块已下载完成, 记录平均下载速度、下载完成时间等需要的信息
        // (省略)更新表BlockFileBean
    }

}
