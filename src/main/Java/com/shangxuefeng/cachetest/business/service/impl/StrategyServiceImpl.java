package com.shangxuefeng.cachetest.business.service.impl;

import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import com.shangxuefeng.cachetest.business.service.StrategyService;
import com.shangxuefeng.cachetest.business.bean.Strategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kevin
 */
@Service
public class StrategyServiceImpl implements StrategyService {
    //@Resource
    //private StrategyDao strategyDao;

    public StrategyServiceImpl(){
        System.out.println("--- StrategyServiceImpl 构造函数 ---");
    }

    public Strategy getStrategyById(int id) {
        return null;//strategyDao.getStrategyById(id);
    }

    public List<Strategy> getStrategyListByPreAction(String preAction) {
        return null;//strategyDao.getStrategyListByPreAction(preAction);
    }
}
