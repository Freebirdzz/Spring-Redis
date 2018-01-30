package com.shangxuefeng.cachetest.commons;

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
 */
public class RedisCache implements Cache {
    //缓存的生存期
    public static final long LIVE_TIME = 86400;
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

    @Override
    public ValueWrapper get(Object key) {
        System.out.println("从RedisCache中获取[" + key  + "]对应的value");
        final Integer keyI = (Integer)key;
        Strategy object = null;
        object = redisTemplate.execute(new RedisCallback<Strategy>() {
            @Override
            public Strategy doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte [] keyB = intToByte4(keyI);
                byte [] valueB = redisConnection.get(keyB);
                if (valueB != null){
                    redisConnection.set(keyB, valueB);
                }
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
        System.out.println("Redis Cache调用put方法[key=" + key + ", value = " + value);
        final String keyf = key.toString();
        final Object valuef = value;
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
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        System.out.println("I'm NULL.Sorry.");

        return null;
    }

    @Override
    public void evict(Object key) {
        System.out.println("-------- RedisCache evict() ----------");
    }

    @Override
    public void clear() {
        System.out.println("-------- RedisCache clear() ----------");
    }


    /**
     * int整数转换为4字节的byte数组
     *
     * @param i  整数
     * @return byte数组
     */
    public static byte[] intToByte4(int i) {
        byte[] targets = new byte[4];
        targets[3] = (byte) (i & 0xFF);
        targets[2] = (byte) (i >> 8 & 0xFF);
        targets[1] = (byte) (i >> 16 & 0xFF);
        targets[0] = (byte) (i >> 24 & 0xFF);
        return targets;
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
             * debug了一下，大致是在write的过程中会判断"obj instanceof Serializable"，如果不成立就抛出了异常
             * 所以解决的时候，对于Strategy这个类，继承了Serializable接口，暂时解决了这个问题
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


}
