package com.vergilyn.demo.springboot.enums.bean;

import com.vergilyn.demo.constant.enums.SongsEnum;

/**
 * 如果简单的转换成json,enum只会有1个值,并不包含枚举中定义的index、singer等.
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/5/6
 */
public class SongsDto {
    private SongsEnum song;

    public SongsDto(SongsEnum song){
        this.song = song;
    }

    public SongsEnum getSong() {
        return song;
    }

    public void setSong(SongsEnum song) {
        this.song = song;
    }
}
