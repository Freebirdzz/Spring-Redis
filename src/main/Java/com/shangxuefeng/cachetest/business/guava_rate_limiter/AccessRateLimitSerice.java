package com.shangxuefeng.cachetest.business.guava_rate_limiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class AccessRateLimitSerice {
    public AccessRateLimitSerice(){
        System.out.println("AccessRateLimitSerice执行构造函数");
    }
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private RateLimiterServiceImpl rateLimiterService;


    public String access(){
        //尝试获取令牌
        if (rateLimiterService.tryAcquire()){

            try{
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
            return "访问接口成功! [" + sdf.format(new Date()) + "]";
        }else{
            return "访问接口被限制 [" + sdf.format(new Date()) + "]";
        }
    }
}
