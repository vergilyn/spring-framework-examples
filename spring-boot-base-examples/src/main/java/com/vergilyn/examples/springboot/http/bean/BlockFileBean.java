package com.vergilyn.examples.springboot.http.bean;

import lombok.Data;
import lombok.ToString;

/**
 * @author VergiLyn
 * @date 2017/11/18
 */
@Data
@ToString
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

    public BlockFileBean(String completeFileId, String blockFileName, String url, long beginOffset, long endOffset) {
        super();
        this.completeFileId = completeFileId;
        this.blockFileName = blockFileName;
        this.beginOffset = beginOffset;
        this.endOffset = endOffset;
        this.downloadUrl = url;
    }
}
