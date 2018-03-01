package guava_rate_limiter;

import com.google.common.util.concurrent.RateLimiter;
import com.shangxuefeng.cachetest.business.guava_rate_limiter.AccessRateLimitSerice;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= "classpath:applicationContext.xml")
public class AccessrateLimitServiceTest {
    //模拟10个线程并发访问
    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    @Resource
    AccessRateLimitSerice accessrateLimitService;

    @Test
    public void testRateLimiter(){
        try {
            Thread.sleep(5000);
            System.out.println("------------开始执行--------------");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("等待了5秒钟来构造令牌");
        for (int i=0;i<30;i++){
            fixedThreadPool.submit(new Runnable(){
               public void run(){
                   System.out.println(accessrateLimitService.access());
               }
            });
        }

        fixedThreadPool.shutdown();
        try {
            fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试基本的RateLimiter
     */
    @Test
    public void testRate(){
        RateLimiter limiter = RateLimiter.create(5);
        for (int i=0;i<10;i++){
            System.out.println("睡眠 " + i + "秒");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("开始执行,已经构造了5个令牌");
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire(4));
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println(limiter.acquire());
        System.out.println("执行结束");

    }
}
