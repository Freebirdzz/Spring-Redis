package com.shangxuefeng.cachetest.business.bean;

import com.google.common.base.MoreObjects;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Strategy {
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
