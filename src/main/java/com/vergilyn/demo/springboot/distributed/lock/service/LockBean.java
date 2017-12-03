package com.vergilyn.demo.springboot.distributed.lock.service;

/**
 * @author VergiLyn
 * @blog http://www.cnblogs.com/VergiLyn/
 * @date 2017/12/2
 */
public class LockBean {
    private Long idic;

    public LockBean(){

    }

    public LockBean(long idic) {
        this.idic = idic;
    }

    public Long getIdic() {
        return idic;
    }

    public void setIdic(Long idic) {
        this.idic = idic;
    }
}
