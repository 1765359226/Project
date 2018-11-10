package com.taotao.Jedis;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Set;

/*
*
* 测试Redis
*
* */
public class TestJedis {
    @Test
    public void testJedis() throws Exception{
        //创建一个jedis对象，需要指定服务ip和端口号
        Jedis jedis = new Jedis("192.168.19.128", 6379);
        //直接操作数据库
        jedis.set("jedis-key","123");
        String s = jedis.get("jedis-key");
        System.out.println(s);
        //关闭jedis
        jedis.close();
    }

    @Test
    public void testJedisPool() throws Exception{
        //创建一个数据库连接池对象（单例），需要指定服务ip和端口号
        JedisPool jedisPool = new JedisPool("192.168.19.128", 6379);
        //从连接池中获取链接
        Jedis jedis = jedisPool.getResource();
        //使用Jedis操作数据库《方法级别使用》
        String s = jedis.get("jedis-key");
        System.out.println(s);
        //一定要关闭jedis链接
        jedis.close();
        //系统关闭前关闭连接池
        jedisPool.close();
    }

     /*
     *
     * redis集群测试
     *
     */
    @Test
    public void testJedisCluster() throws Exception{
//        创建一个JedisCliter对象，构造参数Set类型，集合每个元素是HostAndPort类型-----地址和端口号
        Set<HostAndPort> nodes = new HashSet<>();
        //向集合中添加节点
        nodes.add(new HostAndPort("192.168.19.128",7001));
        nodes.add(new HostAndPort("192.168.19.128",7002));
        nodes.add(new HostAndPort("192.168.19.128",7003));
        nodes.add(new HostAndPort("192.168.19.128",7004));
        nodes.add(new HostAndPort("192.168.19.128",7005));
        nodes.add(new HostAndPort("192.168.19.128",7006));
        JedisCluster jedisCluster = new JedisCluster(nodes);//创建JedisClister对象

//        直接使用JedisCluster操作redis，自带连接池。。jedisClister对象可以是单例的。
//        String key6 = jedisCluster.set("key6", "123");//插入数据
        String s = jedisCluster.get("key6");//获取数据
        System.out.println(s);
//        系统关闭前关闭jedisClister
        jedisCluster.close();
    }
}
