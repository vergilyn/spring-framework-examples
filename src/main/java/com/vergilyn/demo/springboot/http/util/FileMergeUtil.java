package com.vergilyn.demo.springboot.http.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import org.apache.commons.io.IOUtils;

/**
 * 合并文件4种方案:
 * <ol>
 *     <li>利用FileChannel.write()合并文件</li>
 *     <li>利用FileChannel.transferFrom()合并文件</li>
 *     <li>利用apache common-IO, {@link IOUtils#copyLarge(Reader, Writer, char[])}</li>
 *     <li>利用randomAccessFile合并文件</li>
 * </ol>
 * <pre>
 * 只是个人找到的几种方式, 并不清楚他们之间差别, 也未深究哪个性能更好.
 * 简单看实现代码, 只有方式(2)与其他差距较大。
 * <pre/>
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/19
 */
public class FileMergeUtil {

    /**
     * 利用FileChannel.write()合并文件
     *
     * @param dest 最终文件保存完整路径
     * @param files 注意排序
     * @param capacity {@link ByteBuffer#allocate(int)}
     * @see <a href="http://blog.csdn.net/skiof007/article/details/51072885">如何使用java合并多个文件<a/>
     * @see <a href="http://blog.csdn.net/seebetpro/article/details/49184305">ByteBuffer.allocate()与ByteBuffer.allocateDirect()方法的区别<a/>
     */
    public static void channelWrite(String dest, File[] files, int capacity) {
        capacity = capacity <= 0 ? 1024 : capacity;
        FileChannel outChannel = null;
        FileChannel inChannel = null;
        FileOutputStream os = null;
        FileInputStream is = null;
        try {
            os = new FileOutputStream(dest);
            outChannel = os.getChannel();
            for (File file : files) {
                is = new FileInputStream(file);
                inChannel = is.getChannel();
                ByteBuffer bb = ByteBuffer.allocate(capacity);
                while (inChannel.read(bb) != -1) {
                    bb.flip();
                    outChannel.write(bb);
                    bb.clear();
                }
                inChannel.close();
                is.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
                if (inChannel != null) {
                    inChannel.close();
                }
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 利用FileChannel.transferFrom()合并文件
     * @param dest 最终文件保存完整路径
     * @param files 注意排序
     * @see <a href="http://blog.csdn.net/tobacco5648/article/details/52958046">http://blog.csdn.net/tobacco5648/article/details/52958046</a>
     */
    public static void channelTransfer(String dest, File[] files) {
        FileChannel outChannel = null;
        FileChannel inChannel = null;
        FileOutputStream os = null;
        FileInputStream is = null;
        try {
            os = new FileOutputStream(dest);
            outChannel = os.getChannel();
            for (File file : files) {
                is = new FileInputStream(file);
                inChannel = is.getChannel();
                outChannel.transferFrom(inChannel, outChannel.size(), inChannel.size());

                inChannel.close();
                is.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (outChannel != null) {
                    outChannel.close();
                }
                if (inChannel != null) {
                    inChannel.close();
                }
                if (os != null) {
                    os.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 利用apache common-IO, {@link IOUtils#copyLarge(Reader, Writer, char[])}.
     * <p>看实现代码, 不就是普通write()? 没发现又什么特别的优化, 所以感觉此方式性能/效率可能并不好.</p>
     * @param dest
     * @param files
     * @param buffer
     */
    public static void apache(String dest, File[] files, int buffer){
        OutputStream os = null;
        try {
            byte[] buf = new byte[buffer];
            os = new FileOutputStream(dest);
            for (File file : files) {
                InputStream is = new FileInputStream(file);
                IOUtils.copyLarge(is, os, buf);
                is.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 利用randomAccessFile合并文件.
     * <pre>虽然用了RandomAccessFile, 但还是普通的write(), 未了解其性能....<pre/>
     * @param dest
     * @param files
     * @param buffer
     */
    public static void randomAccessFile(String dest, List<File> files, int buffer){
        RandomAccessFile in = null;
        try {
            in = new RandomAccessFile(dest, "rw");
            in.setLength(0);
            in.seek(0);

            byte[] bytes = new byte[buffer];

            int len = -1;
            for (File file : files) {
                RandomAccessFile out = new RandomAccessFile(file, "r");
                while((len = out.read(bytes)) != -1) {
                    in.write(bytes, 0, len);
                }
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
