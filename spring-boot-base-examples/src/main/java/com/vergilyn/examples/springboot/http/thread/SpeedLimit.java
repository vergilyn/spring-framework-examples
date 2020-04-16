package com.vergilyn.examples.springboot.http.thread;

import lombok.ToString;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/26
 */
@ToString
public class SpeedLimit {
    private final Long speed;
    // 已下载大小
    private Long writeSize = 0L;
    private long beginTime;
    private long endTime;

    public SpeedLimit(Long speed, long beginTime) {
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
