package com.vergilyn.demo.springboot.distributed.lock.service;

import java.util.HashMap;
import java.util.Map;

import com.vergilyn.demo.springboot.distributed.lock.annotation.CacheLock;
import com.vergilyn.demo.springboot.distributed.lock.annotation.LockedObject;

import org.springframework.stereotype.Service;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
@Service
public class LockServiceImpl implements LockService {
    public static Map<Long, Long> inventory ;
    static{
        inventory = new HashMap<>();
        inventory.put(10000001L, 10000L);
        inventory.put(10000002L, 10000L);
    }

    @Override
    @CacheLock(lockedPrefix="TEST_PREFIX")
    public void lockMethod(String arg1, @LockedObject Long arg2) {
        //最简单的秒杀，这里仅作为demo示例
        System.out.println("lockMethod: " + reduceInventory(arg2));

    }
    //模拟秒杀操作，姑且认为一个秒杀就是将库存减一，实际情景要复杂的多
    private Long reduceInventory(Long commodityId){
        inventory.put(commodityId,inventory.get(commodityId) - 1);
        return inventory.get(commodityId);
    }
}
