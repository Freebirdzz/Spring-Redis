package com.shangxuefeng.cachetest.business.service;

import com.shangxuefeng.cachetest.business.bean.Strategy;

import java.util.List;

public interface StrategyService {
    Strategy getStrategyById(int id);

    List<Strategy> getStrategyListByPreAction(String preAction);
}
