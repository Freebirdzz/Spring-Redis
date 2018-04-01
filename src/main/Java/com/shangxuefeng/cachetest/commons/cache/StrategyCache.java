package com.shangxuefeng.cachetest.commons.cache;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.shangxuefeng.cachetest.business.bean.Strategy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Resource(name="strategyCache")
public class StrategyCache {
    private static final int MAX = 10;
    private static final int TOTAL = 100;
    private static final int CACHE_SIZE = 10;
    /**
     * LoadingCache 的load方法，对应于get（key）没有得到元素时候，执行的加载操作
     */
    private volatile LoadingCache<Long, Strategy> cityCache = CacheBuilder.newBuilder()
            .maximumSize(CACHE_SIZE)
            .refreshAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, Strategy>() {
                @Override
                public Strategy load(Long key) throws Exception {
                    return generateStg(key);
                }
            });


    private volatile Cache<Long, Strategy> stgCache = CacheBuilder.newBuilder()
            .maximumSize(CACHE_SIZE)
            .expireAfterAccess(1, TimeUnit.MINUTES)
            .build();


    List<Strategy> list = Lists.newArrayList();



    Strategy generateStg(Long id){
        System.out.println("-------Search Strategy[id=" + id + "]");
        for (int i=0;i<list.size();i++){
            if (list.get(i).getId().equals(id)) {
                return list.get(i);
            }
        }

        return null;
    }
    public StrategyCache() {
        System.out.println("--------StrategyCache Constructor Method ----------");
    }

    public Strategy getById(Long id){
        try{
            Strategy stg = cityCache.get(id);
            return stg;
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //匿名内部类，如果要使用外部的参数，参数必须是final的才可以！
    public Strategy getById2(final Long key){
        try {
            // If the key wasn't in the "easy to compute" group, we need to
            // do things the hard way.
            Strategy stg = stgCache.get(key, new Callable<Strategy>() {
                @Override
                public Strategy call() throws ExecutionException {
                    return list.get(Integer.valueOf(key.toString()));
                }
            });
            return stg;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 这个是lambda表达式的callable接口的写法，注意那个空的括号
     * @param key
     * @return
     */
    public Strategy getById3(final Long key){
        try{
            Strategy stg = stgCache.get(key, ()-> {
                return list.get(Integer.valueOf(key.toString()));
            });
            return stg;
        } catch(ExecutionException e){
            e.printStackTrace();
            return null;
        }
    }

}

