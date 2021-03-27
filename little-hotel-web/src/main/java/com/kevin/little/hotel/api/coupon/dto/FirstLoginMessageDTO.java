package com.kevin.little.hotel.api.coupon.dto;

import com.alibaba.fastjson.JSON;

/**
 * 第一次登录消息
 *
 * @author wangyong
 */
public class FirstLoginMessageDTO {

  /**
   * 用户Id
   */
  private Integer userId;

  /**
   * 用户名称
   */
  private String nickname;

  /**
   * 小程序id
   */
  private Integer beId;

  /**
   * 手机号码
   */
  private String phoneNumber;


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public Integer getBeId() {
    return beId;
  }

  public void setBeId(Integer beId) {
    this.beId = beId;
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
