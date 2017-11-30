# HttpDownloadApplication

#### 需求
　　将一个大文件拆分成N个小文件下载, 最后把N个小文件合并成完整的文件.  
　　拆分成N个小块的目的: 如果某块未下载成功, 那么只用重新下载这一块, 不用整个文件重新下载.  

#### 说明
　　代码中用redis模拟了数据库数据.  

##### 1. 获取远程资源ContentLength、FileName
　　实际开发中发现并没有想象中的那么容易得到, 主要还是要看这个download-url是怎么样的, 举例:   
　　a) ex: www.xxx.com/xxx.exe, download-url直接是文件, 那么获取contentLength、fileName都比较简单, 
直接获取HttpURLConnection.getContentLength()就可以获取到, FileName则直接解析download-url(或从Content-Disposition中解析fileName).  
　　b) ex: www.xxx.com/download.html?fileId=1, 这可能有2种情况:  
　　　　1) 直接下载, filename可以在Content-Disposition解析得到, content-length就是资源的真实大小.  
　　　　2) "重定向"或"响应"一个真实下载地址, 那么就需要具体分析. 

##### 2. 保存需下载文件信息, 分割下载文件并生成下载任务
　　a) 表CompleteFileBean保存完整文件的信息, 特别注意字段: isComplete, 用于判断文件是否合并.  
　　b) 表BlockFileBean保存每块的下载任务, isDownload表示是否下载完成.  (分割目的: 只是简单模拟"断点下载", 减少重新下载的)
　　注意事项:  
　　　　1) 我看到的很多下载代码都是用的RandomAccessFile来实现的, 貌似这效率&性能更好.
　　　　2) 实际中要特别小心内存消耗、stream一定要关闭, 不然服务器要炸.  
 
(题外话: 想到的另外种"断点下载", 不用创建这些块, 直接写入完整文件file.exe.tmp, 每次启动下载的时候读取这个file.exe.tmp的size,请求下载的Range就是bytes={size}-{contentLength}.  
感觉这种可能更好, 不知道当初为什么用以上方案做.... >.<! )

##### 3. 执行下载任务
　　sql示例: select * from block_file where is_delete = 0 and is_download = 0;  
　　找到需要下载的任务, 创建下载, 成功下载后维护一下block_file表中需要维护的字段.

##### 4. 执行文件合成任务
　　sql示例: select * from complete_file cf where cf.is_delete = 0 and cf.is_complete = 0 and not exists(select 1 from block_file bf where bf.is_download = 0 and bf.complete_id = cf.id)  
　　找到符合条件的任务, 合并文件. 主要条件: 1) 未合并; 2) 所有"块"都下载完毕.  


#### 备注
　　1、代码中下载未用多线程, 实际中最好用多线程, 避免某块下载过慢导致阻塞.  
　　2、合并文件的几种方式, 个人并不清楚其性能优劣, 但绝对不推荐用BufferStream简单的读写.  
　　3、下载资源文件名获取很麻烦, 具体要分析下载的URL.
　　4、代码中获取contentLength时并未校验这个url是下载地址, 校验一个url是下载url也是很麻烦的一件事情!               