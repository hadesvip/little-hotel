package com.kevin.little.hotel.api.hotel.dto;

import com.alibaba.fastjson.JSON;

/**
 * 酒店房间消息
 *
 * @author wangyong
 */
public class HotelRoomMessageDTO {

  /**
   * 房间id
   */
  private Long roomId;

  /**
   * 手机号
   */
  private String phoneNumber;

  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
