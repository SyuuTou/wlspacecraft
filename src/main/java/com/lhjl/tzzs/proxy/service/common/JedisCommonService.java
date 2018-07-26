package com.lhjl.tzzs.proxy.service.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Component
public class JedisCommonService {

    @Autowired
    private Environment env;

    private JedisPool pool =  null;

    private void initJedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(env.getProperty("jedis.maxIdle")));
        config.setMaxTotal(Integer.valueOf(env.getProperty("jedis.maxTotal")));
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

        pool = new JedisPool(config,env.getProperty("jedis.host"),Integer.valueOf(env.getProperty("jedis.port")),Integer.valueOf(env.getProperty("jedis.timeout")),env.getProperty("jedis.password"));
    }


    public Jedis getJedis(){
        if (null == pool){
            this.initJedisPool();
        }
        return pool.getResource();
    }

    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(5);
        config.setMaxTotal(10);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

//        JedisPool pool = new JedisPool(config,"101.200.44.58",6379,3000,"wixbal292dj");
        JedisPool pool = new JedisPool(config,"localhost",6379,3000,null);
        pool.getResource().set("hello","qwer");
        System.out.println(pool.getResource().get("hello"));

//        Jedis jedis = new Jedis("172.18.154.98");
//        // 查看服务是否运行
//        System.out.println("Server is running: " + jedis.ping());

    }
}
