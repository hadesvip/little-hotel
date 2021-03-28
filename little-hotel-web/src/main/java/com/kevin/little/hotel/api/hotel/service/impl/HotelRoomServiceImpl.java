package com.kevin.little.hotel.api.hotel.service.impl;

import static com.kevin.little.hotel.common.consts.RedisKeyPrefixConst.HOTEL_ROOM_KEY_PREFIX;

import com.alibaba.fastjson.JSON;
import com.kevin.little.hotel.api.hotel.dto.HotelRoomDTO;
import com.kevin.little.hotel.api.hotel.dto.RoomDescriptionDTO;
import com.kevin.little.hotel.api.hotel.dto.RoomPictureDTO;
import com.kevin.little.hotel.api.hotel.enums.HotelBusinessErrorCodeEnum;
import com.kevin.little.hotel.api.hotel.service.HotelRoomService;
import com.kevin.little.hotel.common.base.service.CacheManager;
import com.kevin.little.hotel.common.consts.RedisKeyPrefixConst;
import com.kevin.little.hotel.common.exception.BusinessException;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.mysql.api.MysqlApi;
import com.ruyuan.little.project.mysql.dto.MysqlRequestDTO;
import com.ruyuan.little.project.redis.api.RedisApi;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 酒店服务实现类
 *
 * @author wangyong
 */
@Service
public class HotelRoomServiceImpl implements HotelRoomService {

  private static final Logger logger = LoggerFactory.getLogger(HotelRoomService.class);


  /**
   * mysql dubbo接口api
   */
  @Reference(version = "1.0.0",
      interfaceClass = MysqlApi.class,
      cluster = "failfast")
  private MysqlApi mysqlApi;

  /**
   * redsi dubbo接口api
   */
  @Reference(version = "1.0.0",
      interfaceClass = RedisApi.class,
      cluster = "failfast")
  private RedisApi redisApi;


  @Resource
  private CacheManager<Long, HotelRoomDTO> hotelRoomCacheManager;


  @Override
  public CommonResponse<HotelRoomDTO> queryHotelRoomById(Long id, String phoneNumber) {
    logger.info("获取酒店详情信息,请求参数:id:{},phoneNumber:{}", id, phoneNumber);
    HotelRoomDTO hotelRoomDTO = hotelRoomCacheManager.getByCacheKey(id);
    if (!Objects.isNull(hotelRoomDTO)) {
      logger.info("从jvm缓存中获取到酒店详情,ID:{}", id);
      return CommonResponse.success(hotelRoomDTO);
    }
    logger.warn("jvm缓存中不存在,从redis中获取,hotelRoomId:{}", id);
    CommonResponse<String> response = redisApi
        .get(HOTEL_ROOM_KEY_PREFIX + id, phoneNumber, LittleProjectTypeEnum.ROCKETMQ);
    logger.info("从redis中获取缓存值,响应:{}", JSON.toJSONString(response));
    if (Objects.equals(response.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
      String data = response.getData();
      if (StringUtils.hasLength(data)) {
        // redis缓存不为空
        logger.info("hotelId:{} data hit redis cache ", id);
        return CommonResponse.success(JSON.parseObject(data, HotelRoomDTO.class));
      }
    }
    // redis宕机或查询jvm内存为空 查询db
    logger.info("hotelId:{} data local cache and redis cache miss try from db query", id);
    return CommonResponse.success(this.getHotelRoomById(id, phoneNumber));
  }


  /**
   * 根据房间id查询房间内容
   *
   * @param id          房间id
   * @param phoneNumber 手机号
   * @return 房间信息
   */
  private HotelRoomDTO getHotelRoomById(Long id, String phoneNumber) {
    MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
    mysqlRequestDTO.setSql("SELECT "
        + "id,"
        + "title, "
        + "pcate, "
        + "thumb_url, "
        + "description, "
        + "goods_banner, "
        + "marketprice, "
        + "productprice, "
        + "total,"
        + "createtime "
        + "FROM "
        + "t_shop_goods "
        + "WHERE "
        + "id = ?");
    mysqlRequestDTO.setPhoneNumber(phoneNumber);
    mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
    mysqlRequestDTO.setParams(Collections.singletonList(id));
    logger.info("start query room detail param:{}", JSON.toJSONString(mysqlRequestDTO));
    CommonResponse<List<Map<String, Object>>> commonResponse = mysqlApi.query(mysqlRequestDTO);
    logger.info("end query room detail param:{}, response:{}", JSON.toJSONString(mysqlRequestDTO),
        JSON.toJSONString(commonResponse));
    if (Objects.equals(commonResponse.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
      List<Map<String, Object>> mapList = commonResponse.getData();
      if (!CollectionUtils.isEmpty(mapList)) {
        List<HotelRoomDTO> hotelRoomDetailList = mapList.stream().map(map -> {
          HotelRoomDTO detailDTO = new HotelRoomDTO();
          detailDTO.setId((Long) map.get("id"));
          detailDTO.setTitle(String.valueOf(map.get("title")));
          detailDTO.setpCate((Long) map.get("pcate"));
          detailDTO.setThumbUrl(String.valueOf(map.get("thumb_url")));
          detailDTO.setRoomDescription(
              JSON.parseObject(String.valueOf(map.get("description")), RoomDescriptionDTO.class));
          String goods_banner = String.valueOf(map.get("goods_banner"));
          List<RoomPictureDTO> roomPictures = JSON.parseArray(goods_banner, RoomPictureDTO.class);
          detailDTO.setGoodsBanner(roomPictures);
          detailDTO.setMarketPrice((BigDecimal) map.get("marketprice"));
          detailDTO.setProductPrice((BigDecimal) map.get("productprice"));
          detailDTO.setTotal((Integer) map.get("total"));
          detailDTO.setCreateTime((Long) map.get("createtime"));
          return detailDTO;
        }).collect(Collectors.toList());

        // 存入redis缓存
        HotelRoomDTO hotelRoom = hotelRoomDetailList.get(0);
        redisApi.set(HOTEL_ROOM_KEY_PREFIX + id,
            JSON.toJSONString(hotelRoom),
            phoneNumber,
            LittleProjectTypeEnum.ROCKETMQ);

        // 本地缓存
        hotelRoomCacheManager.update(hotelRoom);

        return hotelRoom;
      }
    }
    logger.info("hotelId:{} data db not exist", id);
    // 房间不存在
    throw new BusinessException(HotelBusinessErrorCodeEnum.HOTEL_ROOM_NOT_EXIST.getMsg());
  }
}
