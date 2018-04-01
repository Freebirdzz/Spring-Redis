package business.dao;

import com.shangxuefeng.cachetest.business.bean.Strategy;
import com.shangxuefeng.cachetest.business.dao.StrategyDao;
import com.shangxuefeng.cachetest.business.enums.StrategyEnum;
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


    @Test
    public void insertStrategyTest(){
        Strategy stg = new Strategy();
        stg.setName("测试策略1");
        stg.setId(16);
        stg.setStatus(2);
        //枚举类型，直接插入数据库的int字段，会报错！需要通过一个BaseTypeHandler这个东西解决
        stg.setType(StrategyEnum.FOUR);
        stg.setCtime(1510297830000L);
        stg.setUtime(1510297830000L);
        stg.setPreAction("BUBBLING");
        System.out.println(stg);
        strategyDao.insertStrategy(stg);
    }

    private String [] action = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight"};
    @Test
    public void batchInsert(){
        int size = 500000;
        for (int i=165023;i<=size;i++){
            Strategy stg = new Strategy();
            stg.setName("批量数据[" + i+ "]");
            stg.setStatus(1);
            stg.setId(i);
            stg.setType(StrategyEnum.codeOf(i%4));
            stg.setCtime(System.currentTimeMillis());
            stg.setUtime(System.currentTimeMillis());
            stg.setPreAction(action[i%8]);
            System.out.println(i);
            strategyDao.insertStrategy(stg);
        }
    }


    @Test
    public void tc(){
        int a = 1;
        int b = 34;
        int c = a = b;
        System.out.println(a+"  " + b + "  " + c);
    }
}
