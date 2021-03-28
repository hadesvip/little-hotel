package com.kevin.little.hotel.api.hotel.consumer;

import com.kevin.little.hotel.api.hotel.listener.HotelRoomUpdateMessageListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 酒店详情消费者信息
 *
 * @author wangyong
 */
@Configuration
public class HotelRoomConsumerConfiguration {


  @Value("${rocketmq.namesrv.address}")
  private String namesrvAddress;

  /**
   * 酒店房间topic
   */
  @Value("${rocketmq.hotelRoom.topic}")
  private String hotelRoomTopic;

  /**
   * 酒店房间数据监听的consumer group
   */
  @Value("${rocketmq.hotelRoom.consumer.group}")
  private String hotelRoomConsumerGroup;

  /**
   * 酒店客房消息
   */
  @Bean(value = "hotelRoomConsumer")
  public DefaultMQPushConsumer hotelRoomConsumer(
      HotelRoomUpdateMessageListener hotelRoomUpdateMessageListener) throws MQClientException {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(hotelRoomConsumerGroup);
    consumer.setNamesrvAddr(namesrvAddress);
    consumer.subscribe(hotelRoomTopic, "*");
    consumer.setMessageListener(hotelRoomUpdateMessageListener);
    consumer.start();
    return consumer;
  }


}
