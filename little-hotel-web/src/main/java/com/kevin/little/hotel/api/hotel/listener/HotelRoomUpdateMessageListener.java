package com.kevin.little.hotel.api.hotel.listener;

import static com.kevin.little.hotel.common.consts.RedisKeyPrefixConst.HOTEL_ROOM_KEY_PREFIX;

import com.alibaba.fastjson.JSON;
import com.kevin.little.hotel.api.hotel.dto.HotelRoomDTO;
import com.kevin.little.hotel.api.hotel.dto.HotelRoomMessageDTO;
import com.kevin.little.hotel.common.base.service.CacheManager;
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
import org.springframework.stereotype.Component;

/**
 * 房间更新成功的listener
 *
 * @author wangyong
 */
@Component
public class HotelRoomUpdateMessageListener implements MessageListenerConcurrently {

  private static final Logger logger = LoggerFactory
      .getLogger(HotelRoomUpdateMessageListener.class);


  @Resource
  private CacheManager<Long, HotelRoomDTO> hotelRoomCacheManager;

  @Reference(version = "1.0.0",
      interfaceClass = RedisApi.class,
      cluster = "failfast")
  private RedisApi redisApi;


  @Override
  public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messageExtList,
      ConsumeConcurrentlyContext context) {
    logger.info("处理需要更新缓存信息");
    for (MessageExt messageExt : messageExtList) {
      String body = new String(messageExt.getBody(), StandardCharsets.UTF_8);
      logger.info("消息内容:{}", body);
      try {
        HotelRoomMessageDTO hotelRoomMessageDTO = JSON.parseObject(body, HotelRoomMessageDTO.class);
        Long roomId = hotelRoomMessageDTO.getRoomId();
        CommonResponse<String> commonResponse = redisApi
            .get(HOTEL_ROOM_KEY_PREFIX + roomId,
                hotelRoomMessageDTO.getPhoneNumber(),
                LittleProjectTypeEnum.ROCKETMQ);
        logger.info("从redis中获取缓存,响应参数:{}", JSON.toJSONString(commonResponse));
        if (Objects.equals(commonResponse.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
          // 成功 赋值给jvm内存
          logger.info("更新缓存值:{}", commonResponse.getData());
          hotelRoomCacheManager
              .update(JSON.parseObject(commonResponse.getData(), HotelRoomDTO.class));
        }
      } catch (Exception e) {
        // 消费失败
        logger.info("消费失败:{}", body);
        //消费失败，以后尝试消费
        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
      }
    }
    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
  }
}
