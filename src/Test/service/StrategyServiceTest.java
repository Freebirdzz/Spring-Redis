package service;

import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import com.shangxuefeng.cachetest.business.bean.Strategy;
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
    StrategyDao strategyDao;


    @Resource
    private RedisTemplate<String, String> redisTemplate;

    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations

    @Test
    public void testDao(){
        for (int i=0;i<9;i++) {
            Strategy stg = strategyDao.getStrategyById(i);
            System.out.println(stg);
        }


        List<Strategy> lists = strategyDao.getStrategyListByPreAction("BUBBLING");
        lists.stream().forEach(System.out::println);
    }


    @Test
    public void testSpringRedisTemplate(){
        // -----------------String类型数据操作 start--------------------
        ValueOperations<String, String> stringOperations = redisTemplate.opsForValue();
        // String类型数据存储，不设置过期时间，永久性保存
        stringOperations.set("string1", "草拟吗。。。。");
        // String类型数据存储，设置过期时间为80秒，采用TimeUnit控制时间单位
        stringOperations.set("string2", "Today is a fantastic day!!!", 80, TimeUnit.SECONDS);
        // 判断key值是否存在，存在则不存储，不存在则存储
        stringOperations.setIfAbsent("string1", "wo cao a");
        stringOperations.setIfAbsent("string3", "what the fuck!!!!!");
        String value1 = stringOperations.get("string1");
        String value2 = stringOperations.get("string2");
        System.out.println(value1);
        System.out.println(value2);
    }
}
