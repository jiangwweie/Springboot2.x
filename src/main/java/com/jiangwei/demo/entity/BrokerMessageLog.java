package com.jiangwei.demo.entity;

import java.io.Serializable;
import lombok.Data;

/**
 * broker_message_log
 * @author 
 */
@Data
public class BrokerMessageLog implements Serializable {
    /**
     * 订单消息唯一id
     */
    private String messageId;

    /**
     * 消息内容
     */
    private String message;

    /**
     * 重试次数
     */
    private Integer tryCount;

    /**
     * 投递状态，0投递中，1成功，2失败
     */
    private String status;

    private String nextRetry;

    private String createTime;

    private String updateTime;

    private static final long serialVersionUID = 1L;
}