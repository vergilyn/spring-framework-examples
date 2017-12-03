# 基于redis实现分布式并发锁
博客地址: http://www.cnblogs.com/VergiLyn/p/7968094.html  

## 代码存在问题
1. 消耗redis的连接过多, 频繁的getConnection()、closeConnection();
2. 创建的RedisLock对象太多.

## 代码的改进, 博客最后的改善思路
以上改进代码依然可能存在的问题:
　　　　1) 连接很可能没有正常关闭.
　　　　2) 连接依然过多, 假设并发有1000个, 那一样会产生1000个连接, 且这些连接只会在竞争获取锁完后才会释放.(且产生了1000个RedisLock对象)
　　　　3) 是否可以缓存注解对象?
　　针对问题2), 主要想达到怎么尽可能减少redis连接?
　　比如: 有1000个并发, 其中200个是兑换商品A, 其中300个是兑换商品B, 其中500个是兑换商品C.
　　1、是否可以用单例模式来实现RedisLock?
　　　　对单例、多线程还是很混乱, 不好说. 但如果可行, 会否太影响获取锁的性能?
　　比如兑换商品A的200个并发共用一个redisConnection, 感觉还是合理的, 毕竟互相之间是竞争关系.
　　但商品A、商品B、商品C如果也共用一个redisConnection, 是不是完全不合理?
　　他们之间根本是"并行"的, 相互之间没有一点联系.
　　2、所以, 是否更进一步的实现是: 同一个锁竞争用相同的RedisLock对象和RedisConnection连接.
　　即竞争商品A的200个并发用同一个"redisConnection_A"、"redisLock_A", 商品B的300个并发用同一个"redisConnection_B"、"redisLock_B"？
　　针对问题3), 在代码RedisDistributedLockAop中, 每次都会:
　　　　1) getMethod(pjp): 获取拦截方法.
　　　　2) 通过拦截方法解析出getRedisKey.
　　是不是可以这么实现, 相同的拦截方法只有第一次需要通过反射获取. 之后直接从缓存(如map)中获取到method, 且因为同一个方法, 所能取field也是一样的.
　　比如, 有一下几个方法都需要用到分布式并发锁: