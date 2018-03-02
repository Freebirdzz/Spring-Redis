package com.shangxuefeng.cachetest.business.bean;

import com.google.common.base.MoreObjects;
import com.shangxuefeng.cachetest.business.enums.StrategyEnum;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 实现Serializable接口，为了序列化！！Serializable接口本身是一个空接口，没有内容
 * Serializable接口这是一个标识，告诉程序所有实现了”我”的对象都需要进行序列化
 */
@Setter
@Getter
public class Strategy implements Serializable{
    private Integer id;
    private String name;
    private String preAction;
    private Integer status;
    private Long ctime;
    private Long utime;
    private StrategyEnum type;

    @Override
    public String toString(){
        return ("[id=" + id + ", name=" + name + ", preAction=" + preAction + ", status=" + status  + ", type=" + type.getDesc() + "]");
    }
}
