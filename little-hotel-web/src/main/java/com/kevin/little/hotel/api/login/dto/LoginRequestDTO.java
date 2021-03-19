package com.kevin.little.hotel.api.login.dto;

import com.alibaba.fastjson.JSON;

/**
 * 登录请求
 *
 * @author wangyong
 */
public class LoginRequestDTO {

  /**
   * 用户ID
   */
  private Integer userId;

  /**
   * 用户名
   */
  private String nickname;

  /**
   * 手机号码
   */
  private String phoneNumber;

  /**
   * token
   */
  private String token;

  /**
   * 小程序ID
   */
  private Integer beId;

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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Integer getBeId() {
    return beId;
  }

  public void setBeId(Integer beId) {
    this.beId = beId;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
