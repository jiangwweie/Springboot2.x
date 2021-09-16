package com.jiangwei.demo.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author jiangwei
 * @date 2021/9/16
 */

@Component
public class RedisPruducer {

    private static final String TOPIC_NAME = "test";

    @Autowired
    RedisTemplate redisTemplate;

    public void produce(String msg){
        redisTemplate.convertAndSend(TOPIC_NAME,msg);
    }

}
