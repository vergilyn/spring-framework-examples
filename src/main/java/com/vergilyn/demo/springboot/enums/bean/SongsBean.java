package com.vergilyn.demo.springboot.enums.bean;

import com.vergilyn.demo.constant.enums.SongsEnum;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/9
 */
public class SongsBean {
    private SongsEnum songsEnum;

    private int index;

    private String singer;

    private String name;

    private String date;

    public SongsBean(SongsEnum songsEnum){
        this.songsEnum = songsEnum;
        if(this.songsEnum != null){
            index = this.songsEnum.index;
            singer = this.songsEnum.singer;
            name = this.songsEnum.name;
            date = this.songsEnum.date;
        }
    }

    public SongsEnum getSongsEnum() {
        return songsEnum;
    }

    public int getIndex() {
        return index;
    }

    public String getSinger() {
        return singer;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }
}
