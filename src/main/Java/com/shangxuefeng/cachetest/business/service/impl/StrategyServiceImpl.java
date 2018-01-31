package com.shangxuefeng.cachetest.business.service.impl;

import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import com.shangxuefeng.cachetest.business.service.StrategyService;
import com.shangxuefeng.cachetest.business.bean.Strategy;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author kevin
 */
@Service
public class StrategyServiceImpl implements StrategyService {
    @Resource
    private StrategyDao strategyDao;

    @Override
    public Strategy getStrategyById(int id) {
        return strategyDao.getStrategyById(id);
    }

    @Override
    public List<Strategy> getStrategyListByPreAction(String preAction) {
        return strategyDao.getStrategyListByPreAction(preAction);
    }

    /**
     * 通过id查找对应的Strategy
     * 首先从缓存（以redis作为例子）中进行查找，如果找到就直接返回
     * 如果没找到，从数据库中查找，然后存到缓存中去，然后返回结果
     * @param id
     * @return
     */
    @Override
    @Cacheable(cacheNames="redisCache")
    public Strategy getThroughCacheById(int id) {
        System.out.println("要从数据库中查找[id=" + id + "]");
        Strategy stg = strategyDao.getStrategyById(id);
        return stg;
    }

}
