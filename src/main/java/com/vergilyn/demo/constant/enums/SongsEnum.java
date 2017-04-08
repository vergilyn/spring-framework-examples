package com.vergilyn.demo.constant.enums;

/**
 * @author VergiLyn
 * @bolg http://www.cnblogs.com/VergiLyn/
 * @date 2017/4/8
 */
public enum SongsEnum {
     SAFE_AND_SOUND(1,"Taylor Swift","Safe&Sound","2011-12-26")
    ,SHAKE_IT_OFF(2,"Taylor Swift","Shake It Off","2014-08-19")
    ,Style(3,"Taylor Swift","Style","2015-02-09")
    ,Sound_Of_Silence(4,"Simon & Garfunkel","The Sound Of Silence","1966-01-17")
    ,Better_Man(5,"Little Big Town","Better Man","2016-10-20")
    ,Yesterday_Once_More(6,"Carpenters","Yesterday Once More","1973-05-16")
    ;

    public int index;
    public String singer;
    public String name;
    public String date;

    SongsEnum(int seq, String singer, String name, String date) {
        this.index = seq;
        this.singer = singer;
        this.name = name;
        this.date = date;
    }

    public static void main(String[] args) {
        System.out.println(SongsEnum.Yesterday_Once_More);
    }
}
