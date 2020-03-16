package com.vergilyn.examples.springboot.http.bean;

import lombok.Data;
import lombok.ToString;

/**
 * 完整文件记录
 * @author VergiLyn
 * @date 2017/11/18
 */
@Data
@ToString
public class CompleteFileBean extends AbstractBean{
    // 下载地址
    private String downloadUrl;
    // 文件名
    private String fileName;
    // 文件大小(-1: 未知大小),单位: bytes
    private long contentLength;
    // 文件是否完整
    private boolean isComplete;

    public CompleteFileBean() {
        super();
    }

    public CompleteFileBean(String downloadUrl, String fileName, long contentLength) {
        super();
        this.downloadUrl = downloadUrl;
        this.fileName = fileName;
        this.contentLength = contentLength;
    }

}
