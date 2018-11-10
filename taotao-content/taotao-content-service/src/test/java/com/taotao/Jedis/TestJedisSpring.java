package com.taotao.Jedis;

import com.taotao.jedis.JedisClient;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
public class TestJedisSpring {

    /*
    * JedisClientPool单机版测试
    *
    * */
    /*
    * JedisClientClient集群测试
    *
    * */
    @Test
    public void TestJedisClientCluster() {
     //初始化spring容器
     ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
     //从容器中获取JedisClient对象
        JedisClient jedisClient = applicationContext.getBean(JedisClient.class);//面向接口JedisClient
        //使用JedisClient对象操作redis
     jedisClient.set("key7","1234");
     String key7 = jedisClient.get("key7");
     System.out.println(key7);
 }

}
