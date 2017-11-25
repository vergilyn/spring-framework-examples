package com.vergilyn.demo.springboot.http.thread;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/26
 */
public class SpeedLimit {
    private final Long totalSize;
    private final Long speed;
    // 已下载大小
    private Long writeSize = 0L;
    private long beginTime;
    private long endTime;


    public SpeedLimit(Long totalSize, Long speed, long beginTime) {
        this.totalSize = totalSize;
        this.speed = speed;
        this.beginTime = beginTime;
        this.endTime = beginTime;
    }

    public void updateWrite(int size){
        this.writeSize += size;
    }

    public void updateEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public Long getSpeed() {
        return speed;
    }

    public Long getWriteSize() {
        return writeSize;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public long getEndTime() {
        return endTime;
    }
}
