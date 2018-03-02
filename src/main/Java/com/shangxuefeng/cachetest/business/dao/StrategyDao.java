package com.shangxuefeng.cachetest.business.dao;

import com.shangxuefeng.cachetest.business.bean.Strategy;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyDao {
    Strategy getStrategyById(int id);

    List<Strategy> getStrategyListByPreAction(String preAction);

    int updateStrategy(Strategy strategy);

    void insertStrategy(Strategy strategy);

}
