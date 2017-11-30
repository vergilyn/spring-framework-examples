package com.vergilyn.demo.springboot.http.bean;

/**
 * 完整文件记录
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
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

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
