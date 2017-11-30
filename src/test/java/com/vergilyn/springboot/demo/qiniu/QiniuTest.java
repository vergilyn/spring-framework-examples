package com.vergilyn.springboot.demo.qiniu;


import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.model.FetchRet;
import com.qiniu.util.Auth;

public class QiniuTest {
    public static final String APP_KEY = "FOXetYM19wIIPkwgnJcTMi-Zmzc1RrHQ48Z01joo";
    public static final String APP_SECRET = "zvmiXZxNZrKcr9UR-73xDeUCUzOPdCpQI_FWHpfT";

    public static void main(String[] args) {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //...其他参数参考类注释

        String bucket = "vergilyn";
        String key = "1510814908569774.mp4";
        String remoteSrcUrl = "http://downloadcdn.quklive.com/userDownload/1504852699996767/1510826400408/1510814908569774.mp4";
        Auth auth = Auth.create(APP_KEY, APP_SECRET);
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //抓取网络资源到空间
        try {
            FetchRet fetchRet = bucketManager.fetch(remoteSrcUrl, bucket, key);
            System.out.println(fetchRet.hash);
            System.out.println(fetchRet.key);
            System.out.println(fetchRet.mimeType);
            System.out.println(fetchRet.fsize);
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
        }
    }
}
