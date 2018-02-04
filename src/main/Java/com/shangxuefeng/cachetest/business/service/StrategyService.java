package com.shangxuefeng.cachetest.business.service;

import com.shangxuefeng.cachetest.business.bean.Strategy;

import java.util.List;

public interface StrategyService {
    Strategy getStrategyById(int id);

    List<Strategy> getStrategyListByPreAction(String preAction);

    /**
     * 通过id查找对应的Strategy
     * 首先从缓存（以redis作为例子）中进行查找，如果找到就直接返回
     * 如果没找到，从数据库中查找，然后存到缓存中去，然后返回结果
     * @param id
     * @return
     */
    Strategy getThroughCacheById(int id);


    /**
     * 更新strategy信息，要点是需要清空缓存，防止访问到脏数据
     * @param strategy
     * @return
     */
    int updateStrategy(Strategy strategy);
}
