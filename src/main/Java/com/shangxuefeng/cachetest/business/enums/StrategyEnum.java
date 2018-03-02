package com.shangxuefeng.cachetest.business.enums;

/**
 * @author kevin
 */

public enum StrategyEnum {
    ONE(0, "默认策略"),
    TWO(1, "主页访问"),
    THREE(2, "策略三"),
    FOUR(3, "策略四");
    private int code;
    private String desc;

    StrategyEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public int getCode(){
        return code;
    }

    public String getDesc(){
        return desc;
    }

    public static StrategyEnum codeOf(int code){
        switch(code){
            case 0:return StrategyEnum.ONE;
            case 1:return StrategyEnum.TWO;
            case 2:return StrategyEnum.THREE;
            case 3:return StrategyEnum.FOUR;
        }
        return null;
    }
}
