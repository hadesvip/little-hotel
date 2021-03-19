package com.kevin.little.hotel.api.login.service.impl;

import com.kevin.little.hotel.api.login.dto.LoginRequestDTO;
import com.kevin.little.hotel.api.login.service.LoginService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * 登录服务具体实现
 * @author wangyong
 */
@Service
public class LoginServiceImpl implements LoginService {


//  @Reference(version = "1.0.0",interfaceClass = RedisApi.class,cluster = "failfast")
//  private RedisApi redisApi;

  public void firstLoginDistributeCoupon(LoginRequestDTO loginRequestDTO) {

  }

  public void resetFirstLoginStatus(LoginRequestDTO loginRequestDTO) {

  }
}
