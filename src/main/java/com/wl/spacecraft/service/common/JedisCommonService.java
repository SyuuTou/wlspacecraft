package com.wl.spacecraft.service.common;

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

    private JedisPool pool = null;

    private void initJedisPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(env.getProperty("jedis.maxIdle")));
        config.setMaxTotal(Integer.valueOf(env.getProperty("jedis.maxTotal")));
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

        pool = new JedisPool(config, env.getProperty("jedis.host"), Integer.valueOf(env.getProperty("jedis.port")), Integer.valueOf(env.getProperty("jedis.timeout")), env.getProperty("jedis.password"));
    }


    public Jedis getJedis() {
        if (null == pool) {
            this.initJedisPool();
        }
        return pool.getResource();
    }

    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(100);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);

        //蓝海测试库
//        JedisPool pool = new JedisPool(config,"101.200.44.58",6379,3000,"wixbal292dj");
        //HK-prod
        JedisPool pool = new JedisPool(config, "45.75.202.72", 6379, 5000, "redis");
        //HuaNan-prodan
//        JedisPool pool = new JedisPool(config,"120.79.143.55",6379,3000,"redis");
//        JedisPool pool = new JedisPool(config,"120.79.25.205",6379,3000,"redis");
        //Localhost
//        JedisPool pool = new JedisPool(config,"localhost",6379,3000,"syuutou");
        pool.getResource().set("jiao", "asdf");
        System.out.println(pool.getResource().get("jiao"));

//        Jedis jedis = new Jedis("172.18.154.98");
//        // 查看服务是否运行
//        System.out.println("Server is running: " + jedis.ping());

    }
}

