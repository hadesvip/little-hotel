package com.kevin.little.hotel.api.hotel.enums;

/**
 * 酒店业务错误码
 *
 * @author wangyong
 */
public enum HotelBusinessErrorCodeEnum {

  /**
   * 酒店房间不存在
   */
  HOTEL_ROOM_NOT_EXIST(580, "酒店房间不存在"),

  ;

  private final int code;

  private final String msg;

  HotelBusinessErrorCodeEnum(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public int getCode() {
    return code;
  }

  public String getMsg() {
    return msg;
  }
}
