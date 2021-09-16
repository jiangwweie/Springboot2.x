package com.jiangwei.demo.producer;

import com.jiangwei.demo.dao.BrokerMessageLogDao;
import com.jiangwei.demo.util.Constants;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: com.jiangwei
 * @Date: 2020-07-31
 * @Version: 1.0
 * @Description:
 */
@Component
public class AmqpSender {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    private BrokerMessageLogDao brokerMessageLogMapper;

    public void send(Object obj) throws Exception {
        //rabbitTemplate.setConfirmCallback(callback);
        //消息唯一id
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        //投递到一个不存在的exchange时候，会触发定时任务retry
        rabbitTemplate.convertAndSend("order-exchange", "order.*", obj, correlationData);
    }

    final RabbitTemplate.ConfirmCallback callback = (correlationData, ack, s) -> {
        System.out.println("correlationData:  " + correlationData);
        String messageId = correlationData.getId();
        if (ack) {
            //如果confirm返回成功，则更新状态
            brokerMessageLogMapper.changeBrokerMessageLogStatus(Constants.ORDER_SEND_SUCCESS,System.currentTimeMillis(),messageId);
        } else {
            //失败则进行后续的具体操作，重试或者补偿
            System.out.println("异常处理......");
        }
    };
}
