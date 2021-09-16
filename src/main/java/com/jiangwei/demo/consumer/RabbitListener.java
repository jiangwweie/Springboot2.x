package com.jiangwei.demo.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @author jiangwei
 * @date 2021/9/16
 */

@Component
@Slf4j
public class RabbitListener {


    @org.springframework.amqp.rabbit.annotation.RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "external.external_g", durable = "true"),
                    exchange = @Exchange(value = "external", type = "topic"),
                    key = "#"
            )
    )
    @RabbitHandler
    public void topicListener(@Payload String info, @Headers Map<String, Object> heads, Channel channel) throws IOException {
        System.out.println("info ； " + info);
        heads.forEach((k, v) -> System.out.println(k + " : " + v));
    }

}
