package com.shangxuefeng.cachetest.business.guava_rate_limiter;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterServiceImpl {

    //每秒发出5个令牌
    RateLimiter rateLimiter = RateLimiter.create(5.0);

    /**
     * 尝试获取令牌
     */
    public boolean tryAcquire(){
        return rateLimiter.tryAcquire();
    }

}
