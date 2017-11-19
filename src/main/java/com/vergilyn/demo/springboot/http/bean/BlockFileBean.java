package com.vergilyn.demo.springboot.http.bean;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/18
 */
public class BlockFileBean extends AbstractBean {

    // 关联完整文件的ID
    private String completeFileId;
    // 下载地址
    private String downloadUrl;
    // 分块文件名
    private String blockFileName;
    // 下载偏移起始位置
    private long beginOffset;
    // 下载偏移结束位置
    private long endOffset;
    // 是否下载完成
    private boolean isDownload;

    public BlockFileBean() {
        super();
    }

    public BlockFileBean(String completeFileId, String blockFileName, String url,long beginOffset, long endOffset) {
        super();
        this.completeFileId = completeFileId;
        this.blockFileName = blockFileName;
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.downloadUrl = url;
    }

    public String getCompleteFileId() {
        return completeFileId;
    }

    public void setCompleteFileId(String completeFileId) {
        this.completeFileId = completeFileId;
    }

    public String getBlockFileName() {
        return blockFileName;
    }

    public void setBlockFileName(String blockFileName) {
        this.blockFileName = blockFileName;
    }

    public long getBeginOffset() {
        return beginOffset;
    }

    public void setBeginOffset(long beginOffset) {
        this.beginOffset = beginOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public void setEndOffset(long endOffset) {
        this.endOffset = endOffset;
    }

    public boolean isDownload() {
        return isDownload;
    }

    public void setDownload(boolean download) {
        isDownload = download;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
