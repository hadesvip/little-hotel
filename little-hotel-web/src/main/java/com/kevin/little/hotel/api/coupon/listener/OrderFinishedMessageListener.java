package com.kevin.little.hotel.api.coupon.listener;

import java.util.List;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author wangyong
 */
@Component
public class OrderFinishedMessageListener implements MessageListenerConcurrently {

  private static final Logger logger = LoggerFactory.getLogger(OrderFinishedMessageListener.class);

  @Override
  public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list,
      ConsumeConcurrentlyContext consumeConcurrentlyContext) {
    return null;
  }
}
