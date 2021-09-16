package com.jiangwei.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author jiangwei
 * @date 2021/9/16
 */
@Configuration
@ConditionalOnProperty(prefix = "demo.rabbitmq", name = "enabled", havingValue = "true", matchIfMissing = false)
@Import(RabbitAutoConfiguration.class)
@Slf4j
public class CustomSpringConfig {

    public CustomSpringConfig() {
        log.info("----------------------------------Custom RabbitConfig loading----------------------------------");
    }
}