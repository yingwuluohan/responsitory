package com.fang.service.kafka;

import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Map;

/**
 * Created by fn on 2017/6/8.
 */
public interface KafkaProducerServer  {
    /**
     * kafka发送消息模板
     * @param topic 主题
     * @param value    messageValue
     * @param ifPartition 是否使用分区 0是\1不是
     * @param partitionNum 分区数 如果是否使用分区为0,分区数必须大于0
     * @param role 角色:bbc app erp...
     */
    public Map<String,Object> sndMesForTemplate(String topic, Object value, String ifPartition,
                                                Integer partitionNum, String role);

    /**
     * 根据key值获取分区索引
     * @param key
     * @param partitionNum
     * @return
     */
    public int getPartitionIndex(String key, int partitionNum);

    /**
     * 检查发送返回结果record
     * @param
     * @return
     */
    //@SuppressWarnings("rawtypes")
   // public Map<String,Object> checkProRecord(ListenableFuture<SendResult<String, String>> res);
    public void sendInfo( String topic , Object obj );

}
