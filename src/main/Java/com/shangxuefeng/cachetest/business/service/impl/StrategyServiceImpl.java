package com.shangxuefeng.cachetest.business.service.impl;

import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import com.shangxuefeng.cachetest.business.service.StrategyService;
import com.shangxuefeng.cachetest.business.bean.Strategy;
import com.shangxuefeng.cachetest.commons.exception.MyException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

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



    /**
     * 更新strategy信息，要点是需要清空缓存，防止访问到脏数据
     * @param strategy 更新strategy
     * @return
     */
    @Override
    @CacheEvict(value = "redisCache", key = "#strategy.id")
    public int updateStrategy(Strategy strategy){
        int ret = strategyDao.updateStrategy(strategy);
        System.out.println("更新了数据库中[" + strategy + "]的信息,ret=" + ret);
        return ret;
    }


    /**
     * 模拟长时间事物
     * @param strategy
     * @param needFail 执行失败
     * @return
     */
    @Transactional(rollbackFor = MyException.class)
    public boolean updateStrategyTransactional(Strategy strategy, boolean needFail){
        int ret = strategyDao.updateStrategy(strategy);
/*        for (int i=1;i<=5;i++){
            try {
                Thread.sleep(1000);
                System.out.println("查询线程睡眠" + i + "秒，模拟长时间任务");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        if (needFail){
            System.out.println("模拟事物执行失败");
        }
        return ret == 0;
    }

    @Override
    public void insertStrategy(Strategy strategy) {
        strategyDao.insertStrategy(strategy);
    }

}
