package service;

import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import com.shangxuefeng.cachetest.business.bean.Strategy;
import com.shangxuefeng.cachetest.business.service.StrategyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:applicationContext.xml")
public class StrategyServiceTest {
    @Resource
    StrategyService strategyService;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 利用Dao层，测试通过mybatis实现基本的数据库查询
     */
    @Test
    public void testDao(){
        for (int i=1;i<9;i++) {
            Strategy stg = strategyService.getStrategyById(i);
            System.out.println(stg);
        }
        List<Strategy> lists = strategyService.getStrategyListByPreAction("BUBBLING");
        lists.stream().forEach(System.out::println);
    }

    /**
     * 利用Spring-data提供的RedisTemplate,完成Redis和Spring的集成，实现简单的操作redis
     */
    @Test
    public void testSpringRedisTemplate(){
        // -----------------String类型数据操作 start--------------------
        ValueOperations<String, String> stringOperations = redisTemplate.opsForValue();
        stringOperations.set("1", "Today is a good day");

        System.out.println(stringOperations.get("1"));
/*        // String类型数据存储，不设置过期时间，永久性保存
        stringOperations.set("string1", "草拟吗。。。。");
        // String类型数据存储，设置过期时间为80秒，采用TimeUnit控制时间单位
        stringOperations.set("string2", "Today is a fantastic day!!!", 80, TimeUnit.SECONDS);
        // 判断key值是否存在，存在则不存储，不存在则存储
     //   stringOperations.setIfAbsent("string1", "wo cao a");
     //   stringOperations.setIfAbsent("string3", "what the fuck!!!!!");
        String value1 = stringOperations.get("string1");
        String value2 = stringOperations.get("string2");
        System.out.println(value1);
        System.out.println(value2);

        String name = stringOperations.get("name");
        System.out.println(name);*/
    }


    /**
     * 利用Spring自带的Cachable注解功能，通过Redis，实现Cacheable注解添加缓存层
     * Serice层对于数据库的操作
     * 1、通过@Cacheable注解中指定的缓存（这里是Redis）查找相应的内容是否在缓存中
     * 2、如果在缓存中，直接返回
     * 3、不在缓存中的话，从数据库中进行查询，并将结果放到缓存中去
     */
    @Test
    public void testSpringCachable(){
        System.out.println("第一次查找: ");
        Strategy stg = strategyService.getThroughCacheById(3);
        System.out.println("查询结果：" + stg);
        System.out.println("第二次查找：");
        stg = strategyService.getThroughCacheById(3);
        System.out.println("查询结果：" + stg);
    }
}
