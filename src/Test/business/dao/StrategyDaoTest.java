package business.dao;

import com.shangxuefeng.cachetest.business.bean.Strategy;
import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:applicationContext.xml")
public class StrategyDaoTest {
    @Resource
    StrategyDao strategyDao;

    @Test
    public void basicMyBatis(){
        //根据主键查询,单独结果
        for (int i=0;i<10;i++) {
            Strategy stg = strategyDao.getStrategyById(i);
            if (Objects.isNull(stg)){
                System.out.println("[id=" + i + "] is null.");
            }else{
                System.out.println(stg);
            }
        }


        //查询结果集
        String notEmptyParam = "BUBBLING";
        String emptyParam = "empty";
        List<Strategy> list = strategyDao.getStrategyListByPreAction(notEmptyParam);
        list.forEach(System.out::println);
        System.out.println("-----查询存在的结果集,共查询到" + list.size() + "个结果-----");

        list = strategyDao.getStrategyListByPreAction(emptyParam);
        list.forEach(System.out::println);
        System.out.println("-----查询不存在的结果集,listIsNull=" + Objects.isNull(list) + ", size=" + list.size() + "-----");

    }
}
