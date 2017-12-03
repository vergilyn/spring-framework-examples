package com.vergilyn.demo.springboot.distributed.lock.service;

import java.util.HashMap;
import java.util.Map;

import com.vergilyn.demo.springboot.distributed.lock.annotation.RedisDistributedLock;
import com.vergilyn.demo.springboot.distributed.lock.annotation.RedisLockedKey;

import org.springframework.stereotype.Service;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
@Service
public class LockServiceImpl implements LockService {
    public static Map<Long, Integer> goods;
    static{
        goods = new HashMap<>();
        goods.put(1L, 100);
        goods.put(2L, 200);
    }

    @Override
    @RedisDistributedLock(lockedPrefix="TEST_PREFIX")
    public void lockMethod(String arg1, @RedisLockedKey Long arg2) {
        //最简单的秒杀，这里仅作为demo示例
        System.out.println("lockMethod, goods: " + reduceInventory(arg2));

    }

    @Override
    @RedisDistributedLock(lockedPrefix="TEST_PREFIX")
    public void lockMethod(@RedisLockedKey(field = "idic")LockBean lockBean) {
        System.out.println("lockMethod bean, goods: " + reduceInventory(lockBean.getIdic()));

    }

    // 模拟秒杀操作，姑且认为一个秒杀就是将库存减一
    private Integer reduceInventory(Long commodityId){
        goods.put(commodityId, goods.get(commodityId) - 1);
        return goods.get(commodityId);
    }
}
