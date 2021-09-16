package com.jiangwei.demo.dao;

import com.jiangwei.demo.entity.BrokerMessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BrokerMessageLogDao {
    int deleteByPrimaryKey(String messageId);

    int insert(BrokerMessageLog record);

    int insertSelective(BrokerMessageLog record);

    BrokerMessageLog selectByPrimaryKey(String messageId);

    int updateByPrimaryKeySelective(BrokerMessageLog record);

    int updateByPrimaryKey(BrokerMessageLog record);

    /**
     * 查询消息状态为0（发送中）且已经超时的消息集合
     * @param now_time
     * @return List<BrokerMessageLog>
     */
    List<BrokerMessageLog> query4StatusAndTimeoutMessage(Long now_time);

    /**
     * 重新发送，统计count次数+1
     * @param   update_time
     * @param message_id
     * @return
     */
    int update4ReSend(@Param("update_time") Long update_time , @Param("message_id") String message_id);


    /**
     * 更新最终消息发送结果 成功 失败
     * @param  status
     * @param update_time
     * @param message_id
     * @return
     */
    int changeBrokerMessageLogStatus(@Param("status")String status ,@Param("update_time") Long update_time , @Param("message_id") String message_id);
}