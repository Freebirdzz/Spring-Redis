package com.shangxuefeng.cachetest.business.bean;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Strategy implements Serializable{
    private Integer id;
    private String name;
    private String preAction;
    private Integer status;
    private Long ctime;
    private Long utime;


    @Override
    public String toString(){
        return ("[id=" + id + ", name=" + name + ", preAction=" + preAction + ", status=" + status + "]");
    }
}
