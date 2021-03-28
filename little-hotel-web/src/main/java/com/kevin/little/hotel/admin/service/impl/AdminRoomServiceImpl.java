package com.kevin.little.hotel.admin.service.impl;

import static com.kevin.little.hotel.common.consts.RedisKeyPrefixConst.HOTEL_ROOM_KEY_PREFIX;

import com.alibaba.fastjson.JSON;
import com.kevin.little.hotel.admin.dto.AdminHotelRoomDTO;
import com.kevin.little.hotel.admin.dto.AdminHotelRoomMessageDTO;
import com.kevin.little.hotel.admin.dto.AdminRoomDescriptionDTO;
import com.kevin.little.hotel.admin.dto.AdminRoomPictureDTO;
import com.kevin.little.hotel.admin.service.AdminRoomService;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.mysql.api.MysqlApi;
import com.ruyuan.little.project.mysql.dto.MysqlRequestDTO;
import com.ruyuan.little.project.redis.api.RedisApi;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author wangyong
 */
@Service
public class AdminRoomServiceImpl implements AdminRoomService {

  private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoomServiceImpl.class);

  /**
   * mysql dubbo服务
   */
  @Reference(version = "1.0.0",
      interfaceClass = MysqlApi.class,
      cluster = "failfast")
  private MysqlApi mysqlApi;

  /**
   * redis dubbo服务
   */
  @Reference(version = "1.0.0",
      interfaceClass = RedisApi.class,
      cluster = "failfast")
  private RedisApi redisApi;

  /**
   * 房间管理mq producer
   */
  @Autowired
  @Qualifier(value = "hotelRoomMqProducer")
  private DefaultMQProducer hotelRoomMqProducer;

  /**
   * 酒店房间topic
   */
  @Value("${rocketmq.hotelRoom.topic}")
  private String hotelRoomTopic;

  @Override
  public CommonResponse add(AdminHotelRoomDTO adminHotelRoom) {
    // TODO 房间数据mysql已经存储 这里只是请求转发过来写redis
    redisApi.set(HOTEL_ROOM_KEY_PREFIX + adminHotelRoom.getId(),
        JSON.toJSONString(adminHotelRoom),
        adminHotelRoom.getPhoneNumber(),
        LittleProjectTypeEnum.ROCKETMQ);
    LOGGER.info("add hotel room to redis cache roomId:{}", adminHotelRoom.getId());
    return CommonResponse.success();
  }

  @Override
  public CommonResponse update(AdminHotelRoomDTO hotelRoom) {
    String phoneNumber = hotelRoom.getPhoneNumber();
    MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
    mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
    mysqlRequestDTO.setPhoneNumber(phoneNumber);
    mysqlRequestDTO.setSql(
        "UPDATE t_shop_goods SET pcate = ?, title = ?, thumb_url = ?, productprice = ?, total = ?, totalcnf = ? WHERE id = ?");
    this.builderSqlParams(hotelRoom, mysqlRequestDTO);
    LOGGER.info("start update hotel room param:{}", JSON.toJSONString(mysqlRequestDTO));
    // 写mysql
    CommonResponse<Integer> response = mysqlApi.update(mysqlRequestDTO);
    LOGGER.info("end update hotel room param:{}, response:{}", JSON.toJSONString(mysqlRequestDTO),
        JSON.toJSONString(response));

    // db更新成功后，更新redis，发客房数据更新的消息
    if (Objects.equals(response.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
      Long roomId = hotelRoom.getId();

      // 查询更新后的数据
      AdminHotelRoomDTO adminHotelRoom = this.getHotelRoomById(roomId, phoneNumber);

      LOGGER.info("update hotel room data success update redis cache key:{}",
          HOTEL_ROOM_KEY_PREFIX + roomId);
      // 写redis
      redisApi.set(HOTEL_ROOM_KEY_PREFIX + roomId, JSON.toJSONString(adminHotelRoom), phoneNumber,
          LittleProjectTypeEnum.ROCKETMQ);

      // 发客房数据更新的消息
      this.sendRoomUpdateMessage(phoneNumber, roomId);

    }
    return response;
  }

  /**
   * 发客房数据更新的消息到mq中
   *
   * @param phoneNumber 手机号
   * @param roomId      房间id
   */
  private void sendRoomUpdateMessage(String phoneNumber, Long roomId) {
    // 发送广播消息到mq中
    // 提供给小程序的api模块来消费消息后从redis中获取消息更新本地jvm内存
    Message message = new Message();
    message.setTopic(hotelRoomTopic);
    AdminHotelRoomMessageDTO adminHotelRoomMessage = new AdminHotelRoomMessageDTO();
    adminHotelRoomMessage.setRoomId(roomId);
    adminHotelRoomMessage.setPhoneNumber(phoneNumber);

    message.setBody(JSON.toJSONString(adminHotelRoomMessage).getBytes(StandardCharsets.UTF_8));
    try {
      LOGGER.info("start send hotel room update  message, param:{}", roomId);
      SendResult sendResult = hotelRoomMqProducer.send(message);
      LOGGER.info("end send hotel room update  message, param:{}, sendResult:{}", roomId,
          JSON.toJSONString(sendResult));
    } catch (Exception e) {
      LOGGER.error("send login success notify message fail, error message:{}", e);
    }
  }

  /**
   * 根据房间id查询房间内容
   *
   * @param id          房间id
   * @param phoneNumber 用户手机号
   * @return 房间信息
   */
  private AdminHotelRoomDTO getHotelRoomById(Long id, String phoneNumber) {
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
    LOGGER.info("start query room detail param:{}", JSON.toJSONString(mysqlRequestDTO));
    CommonResponse<List<Map<String, Object>>> commonResponse = mysqlApi.query(mysqlRequestDTO);
    LOGGER.info("end query room detail param:{}, response:{}", JSON.toJSONString(mysqlRequestDTO),
        JSON.toJSONString(commonResponse));
    if (Objects.equals(commonResponse.getCode(), ErrorCodeEnum.SUCCESS.getCode())) {
      List<Map<String, Object>> mapList = commonResponse.getData();
      List<AdminHotelRoomDTO> hotelRoomDetailList = mapList.stream().map(map -> {
        AdminHotelRoomDTO detailDTO = new AdminHotelRoomDTO();
        detailDTO.setId((Long) map.get("id"));
        detailDTO.setTitle(String.valueOf(map.get("title")));
        detailDTO.setpCate((Long) map.get("pcate"));
        detailDTO.setThumbUrl(String.valueOf(map.get("thumb_url")));
        detailDTO.setRoomDescription(JSON.parseObject(String.valueOf(map.get("description")),
            AdminRoomDescriptionDTO.class));
        String goods_banner = String.valueOf(map.get("goods_banner"));
        List<AdminRoomPictureDTO> adminRoomPictures = JSON
            .parseArray(goods_banner, AdminRoomPictureDTO.class);
        detailDTO.setGoodsBannerList(adminRoomPictures);
        detailDTO.setMarketPrice((BigDecimal) map.get("marketprice"));
        detailDTO.setProductPrice((BigDecimal) map.get("productprice"));
        detailDTO.setTotal((Integer) map.get("total"));
        detailDTO.setCreateTime((Long) map.get("createtime"));
        return detailDTO;
      }).collect(Collectors.toList());
      return hotelRoomDetailList.get(0);
    }
    return null;
  }

  /**
   * 构建sql查询条件
   *
   * @param adminHotelRoom  房间数据
   * @param queryRequestDTO mysql查询请求体
   */
  private void builderSqlParams(AdminHotelRoomDTO adminHotelRoom, MysqlRequestDTO queryRequestDTO) {
    List<Object> params = new ArrayList<>();
    params.add(adminHotelRoom.getpCate());
    params.add(adminHotelRoom.getTitle());
    params.add(adminHotelRoom.getThumbUrl());
    params.add(adminHotelRoom.getProductPrice());
    params.add(adminHotelRoom.getTotal());
    params.add(adminHotelRoom.getTotalCnf());
    params.add(adminHotelRoom.getId());
    queryRequestDTO.setParams(params);
  }
}
