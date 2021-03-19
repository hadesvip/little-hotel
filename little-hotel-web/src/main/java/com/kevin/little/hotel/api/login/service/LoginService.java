package com.kevin.little.hotel.api.login.service;

import com.kevin.little.hotel.api.login.dto.LoginRequestDTO;

/**
 * 登录服务
 *
 * @author wangyong
 */
public interface LoginService {


  /**
   * 第一次登录分发优惠券
   */
  void firstLoginDistributeCoupon(LoginRequestDTO loginRequestDTO);

  /**
   * 重置用户状态
   */
  void resetFirstLoginStatus(LoginRequestDTO loginRequestDTO);

}
