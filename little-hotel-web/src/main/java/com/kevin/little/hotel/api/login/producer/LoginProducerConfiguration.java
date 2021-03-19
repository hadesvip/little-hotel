package com.kevin.little.hotel.api.login.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginProducerConfiguration {

  @Value("${rocketmq.namesrv.address}")
  private String namesrvAddress;

  @Value("${rocketmq.login.producer.group}")
  private String loginProducerGroup;


  /**
   * 登录生产者
   */
  @Bean
  public DefaultMQProducer loginMqProducer() throws MQClientException {
    DefaultMQProducer loginMqProducer = new DefaultMQProducer(loginProducerGroup);
    loginMqProducer.setNamesrvAddr(namesrvAddress);
    loginMqProducer.start();
    return loginMqProducer;
  }


}
