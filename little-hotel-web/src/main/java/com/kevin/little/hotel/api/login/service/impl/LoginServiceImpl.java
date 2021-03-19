package com.kevin.little.hotel.api.login.service.impl;

import com.kevin.little.hotel.api.login.dto.LoginRequestDTO;
import com.kevin.little.hotel.api.login.service.LoginService;
import com.ruyuan.little.project.redis.api.RedisApi;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;

/**
 * 登录服务具体实现
 *
 * @author wangyong
 */
@Service
public class LoginServiceImpl implements LoginService {

  @Reference(version = "1.0.0", interfaceClass = RedisApi.class, cluster = "failfast")
  private RedisApi redisApi;

  @Override
  public void firstLoginDistributeCoupon(LoginRequestDTO loginRequestDTO) {

  }

  @Override
  public void resetFirstLoginStatus(LoginRequestDTO loginRequestDTO) {

  }
}
