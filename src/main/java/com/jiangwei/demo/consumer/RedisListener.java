package com.jiangwei.demo.consumer;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author jiangwei
 * @date 2021/9/16
 */
@Component
public class RedisListener implements ApplicationRunner {

    private static final String TOPIC_NAME = "test";

    @Autowired
    RedisTemplate redisTemplate;


    private void onMsg() {
        RedisConnection connection = redisTemplate.getConnectionFactory().getConnection();
        connection.subscribe((message, bytes) -> System.out.println(new String(message.getBody())), TOPIC_NAME.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.onMsg();
    }
}
