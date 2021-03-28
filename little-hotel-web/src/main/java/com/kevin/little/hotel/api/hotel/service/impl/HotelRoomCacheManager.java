package com.kevin.little.hotel.api.hotel.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.collect.Maps;
import com.kevin.little.hotel.api.hotel.dto.HotelRoomDTO;
import com.kevin.little.hotel.common.base.service.CacheManager;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 酒店房间缓存
 *
 * @author wangyong
 */
@Service
public class HotelRoomCacheManager implements CacheManager<Long, HotelRoomDTO> {

  private static final Logger logger = LoggerFactory.getLogger(HotelRoomCacheManager.class);

  public final Cache<Long, HotelRoomDTO> HOTEL_ROOM_CACHE = CacheBuilder.newBuilder()
      .maximumSize(1000)
      .build();


  @Override
  public HotelRoomDTO getByCacheKey(Long hotelRoomId) {
    logger.info("获取酒店房间信息,hotelRoomId:{}", hotelRoomId);
    HotelRoomDTO hotelRoomDTO = null;
    try {
      hotelRoomDTO = HOTEL_ROOM_CACHE.get(hotelRoomId, () -> {
        logger.warn("缓存中酒店房间信息为空,hotelRoomId:{}", hotelRoomId);
        return null;
      });
    } catch (ExecutionException e) {
      logger.error("加载缓存数据发生异常", e);
    }
    return hotelRoomDTO;
  }

  @Override
  public void update(HotelRoomDTO update) {
    logger.info("更新酒店:{},对应的缓存", JSON.toJSONString(update));
    HOTEL_ROOM_CACHE.put(update.getId(), update);
    logger.info("更新酒店缓存成功");
  }

  @Override
  public void put(Long hotelRoomId, HotelRoomDTO hotelRoomDTO) {
    logger.info("设置酒店缓存,key:{},value:{}", hotelRoomId, hotelRoomDTO);
    HOTEL_ROOM_CACHE.put(hotelRoomId, hotelRoomDTO);
  }


}
