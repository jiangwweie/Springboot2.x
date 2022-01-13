package com.jiangwei.demo;

import com.jiangwei.demo.config.BinanceConfig;
import com.jiangwei.demo.dao.UserDao;
import com.jiangwei.demo.entity.UserEntity;
import com.jiangwei.demo.producer.RedisPruducer;
import com.jiangwei.demo.service.BinanceService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangwei
 */
@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@RestController
@MapperScan("com.jiangwei.demo.dao")
@EnableConfigurationProperties(value = {BinanceConfig.class})
public class DemoApplication {

    @Autowired
    RedisPruducer redisPruducer;

    @Autowired
    BinanceService binanceService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/hello/{msg}")
    public String hello(@PathVariable String msg) {
        //redisPruducer.produce(msg);
        return binanceService.getTickerPrice(msg).toString();
    }

    @Autowired
    UserDao userDao;

    @GetMapping("/user/{id}")
    public UserEntity hello(@PathVariable Integer id) {

        return userDao.selectByPrimaryKey(id);
    }

}
