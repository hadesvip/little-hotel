package com.kevin.little.hotel.api.coupon.listener;

import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
import static org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus.RECONSUME_LATER;

import com.alibaba.fastjson.JSON;
import com.kevin.little.hotel.api.coupon.dto.FirstLoginMessageDTO;
import com.kevin.little.hotel.api.coupon.service.CouponService;
import com.kevin.little.hotel.common.consts.RedisKeyPrefixConst;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.redis.api.RedisApi;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * 第一次登录消息监听
 *
 * @author wangyong
 */
@Component
public class FirstLoginMessageListener implements MessageListenerConcurrently {

  private static final Logger logger = LoggerFactory.getLogger(FirstLoginMessageListener.class);

  @Resource
  private CouponService couponService;

  /**
   * 第一次登陆下发的优惠券id
   */
  @Value("${first.login.couponId}")
  private Integer firstLoginCouponId;

  /**
   * 第一次登陆优惠券有效天数
   */
  @Value("${first.login.coupon.day}")
  private Integer firstLoginCouponDay;


  /**
   * redis dubbo服务
   */
  @Reference(version = "1.0.0",
      interfaceClass = RedisApi.class,
      cluster = "failfast")
  private RedisApi redisApi;


  @Override
  public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageList,
      ConsumeConcurrentlyContext consumeConcurrentlyContext) {
    logger.info("监听到第一次登录消息，内容:{}", messageList);
    if (CollectionUtils.isEmpty(messageList)) {
      logger.warn("消息内容为空，无需处理");
      return CONSUME_SUCCESS;
    }
    for (MessageExt messageExt : messageList) {
      String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
      logger.info("接收第一次登录消息内容:{}", body);
      FirstLoginMessageDTO firstLoginMessageDTO = JSON
          .parseObject(body, FirstLoginMessageDTO.class);
      Integer userId = firstLoginMessageDTO.getUserId();
      String phoneNumber = firstLoginMessageDTO.getPhoneNumber();
      Integer beId = firstLoginMessageDTO.getBeId();
      String cacheKey = RedisKeyPrefixConst.FIRST_LOGIN_DUPLICATION_KEY_PREFIX + userId;
      try {
        CommonResponse<Boolean> response = redisApi.setnx(cacheKey, String.valueOf(userId),
            phoneNumber,
            LittleProjectTypeEnum.ROCKETMQ);
        if (Objects.equals(response.getCode(), ErrorCodeEnum.SUCCESS.getCode()) && Objects
            .equals(response.getData(), Boolean.FALSE)) {
          logger.warn("重复消费第一次登录消息,直接返回");
          continue;
        }
        //未重复消费,分发权益
        else {
          //分发优惠券
          couponService.distributeCoupon(beId, userId, firstLoginCouponId, firstLoginCouponDay,
              0,
              phoneNumber);
          logger.info("分发优惠券成功,userId:{},phoneNumber:{}", userId, phoneNumber);
        }
      } catch (Exception e) {
        logger.warn("消费第一次登录消息失败,内容:{}", body, e);
        //消费失败,删除redis中key
        if (!Objects.isNull(userId)) {
          redisApi.del(cacheKey, phoneNumber, LittleProjectTypeEnum.ROCKETMQ);
        }
        //消费失败,稍后重试
        return RECONSUME_LATER;
      }
    }

    return CONSUME_SUCCESS;
  }
}
