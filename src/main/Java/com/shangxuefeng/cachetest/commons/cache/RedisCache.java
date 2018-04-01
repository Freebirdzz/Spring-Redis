package com.shangxuefeng.cachetest.commons.cache;

import com.shangxuefeng.cachetest.business.bean.Strategy;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.*;
import java.util.concurrent.Callable;

/**
 * Cache是Spring提供的一个抽象，相当于提供一个cache使用的模板，使用的时候使用具体的缓存进行填充就行
 * 所以我们使用的时候：
 * 1、根据自己使用的实际缓存（redis，guava cache，ConcurrentHashMap等等）
 * 2、配置相关的信息
 * 3、利用使用的实际缓存，实现Cache的方法
 *
 *
 *  注意！Cache不支持key为null的情况！！！！如果需要支持，使用AbstractValueAdaptingCache抽象类
 */
public class RedisCache implements Cache {
    //缓存的生存期
    public static final long LIVE_TIME = 5;
    /**
     * 这里我们使用的是Redis，为了方便，用Spring提供的RedisTemplate来操作Redis
     * 在这个例子中，用Redis来缓存Strategy的id和Strategy对象
     */
    private RedisTemplate<Integer, Strategy> redisTemplate;
    /**
     *
     */
    private String name;

    public RedisTemplate<Integer, Strategy> getRedisTemplate(){
        return redisTemplate;
    }
    public void setRedisTemplate(RedisTemplate<Integer, Strategy> redisTempalte){
        this.redisTemplate = redisTempalte;
    }

    @Override
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object getNativeCache() {
        return this.redisTemplate;
    }

    /**
     * 这个get方法，通过反序列化的形式，获取redis中存储的value值
     * @param key
     * @return
     */
    @Override
    public ValueWrapper get(Object key) {
        System.out.println("从RedisCache中获取[id=" + key  + "]对应的value");
        final Integer keyI = (Integer)key;

        /**
         *    对应下面的set方法
         *    object = redisTemplate.opsForValue().get(key);
         */

        Object object = null;
        object = redisTemplate.execute(new RedisCallback<Strategy>() {
            @Override
            public Strategy doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte [] valueB = redisConnection.get(keyI.toString().getBytes());

                return valueB == null ? null : toObject(valueB);
            }
        });
        return ((object != null) ? new SimpleValueWrapper(object) : null);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        System.out.println("I'm NULL.Sorry.");
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        System.out.println("method[get(Object key, Callable<T>)] I'm NULL.Sorry.");

        return null;
    }

    @Override
    public void put(Object key, Object value) {
        System.out.println("Redis Cache调用put方法[key=" + key + ", value = " + value + "]");
        final String keyf = key.toString();
        final Object valuef = value;

        /**
         * redisTemplate.opsForValue().set((Integer)key, (Strategy)value);
         * 注意，上面这种set方式，有两个操作：
         * 1、对key进行序列化，得到结果keyTmp
         * 2、将这个keyTmp存入redis中去。
         *
         * 所以，如果想通过redis-cli来访问设置的key，需要用keyTmp来查找！
         */

        /*可以写成下面的lambda的写法
        redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte [] keyb = keyf.getBytes();
                byte [] valueb = toByteArray(valuef);
                redisConnection.set(keyb, valueb);
                redisConnection.expire(keyb, LIVE_TIME);
                return 1L;
            }
        });
        */
        redisTemplate.execute((RedisCallback<Long>) redisConnection -> {
            byte [] keyb = keyf.getBytes();
            byte [] valueb = toByteArray(valuef);
            redisConnection.set(keyb, valueb);
            redisConnection.expire(keyb, LIVE_TIME);
            return 1L;
        });



    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        System.out.println("I'm NULL.Sorry.");

        return null;
    }

    @Override
    public void evict(Object key) {
        System.out.println("删除缓存中[key=" + key + "]元素");
        final String keyf = key.toString();
        redisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.del(keyf.getBytes()));
    }

    @Override
    public void clear() {
        System.out.println("-------- RedisCache clear() ----------");
    }



    /**
     * 反序列化过程
     * @param bytes
     * @return
     */
    private Strategy toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return (Strategy)obj;
    }

    /**
     * 序列化过程
     * @param obj
     * @return
     */
    private byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            /**
             * 这个writeObject方法要注意，这个方法开始的时候会报一个错误：
             * java.io.NotSerializableException: com.shangxuefeng.cachetest.business.bean.Strategy
             * debug了一下，writeObject的过程大致是：
             * 1、生成一个描述被序列化对象的类的类元信息的ObjectStreamClass对象。
             * 2、根据传入的需要序列化的对象的实际类型进行不同的序列化操作。
             *      2.1、从代码里面可以很明显的看到，对于String类型、数组类型和Enum可以直接进行序列化。
             *      2.2、如果被序列化对象实现了Serializable对象，则会调用writeOrdinaryObject()方法进行序列化。
             *      2.3、以上两个都不符合，抛出NotSerializableException异常
             */
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }


    public static final Long DAYS = 30L;
    public static final Long haha = DAYS * 24 * 3600 * 1000;
    public static void main(String [] args){
            Long cur = System.currentTimeMillis();
            System.out.println(cur);
            Long ti = System.currentTimeMillis() - haha;
            System.out.println("tmp = " + haha + ", ti = " + ti);
    }

}
