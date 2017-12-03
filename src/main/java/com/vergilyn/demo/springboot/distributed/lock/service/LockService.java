package com.vergilyn.demo.springboot.distributed.lock.service;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/11/30
 */
public interface LockService {
    public void lockMethod(String arg1,Long arg2);

    public void lockMethod(LockBean lockBean);
}
