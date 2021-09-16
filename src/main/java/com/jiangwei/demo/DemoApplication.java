package com.jiangwei.demo;

import com.jiangwei.demo.producer.RedisPruducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangwei
 */
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@RestController
public class DemoApplication {

    @Autowired
    RedisPruducer redisPruducer;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello/{msg}")
    public String hello(@PathVariable String msg) {
        redisPruducer.produce(msg);
        return "hello world";
    }

}
