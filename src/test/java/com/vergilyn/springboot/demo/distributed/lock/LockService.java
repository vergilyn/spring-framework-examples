package com.vergilyn.springboot.demo.distributed.lock;

import com.vergilyn.demo.springboot.distributed.lock.annotation.CacheLock;
import com.vergilyn.demo.springboot.distributed.lock.annotation.LockedObject;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public interface LockService {
    @CacheLock(lockedPrefix="TEST_PREFIX")
    public void lockMethod(String arg1,@LockedObject Long arg2);
}
