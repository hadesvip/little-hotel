package com.kevin.little.hotel.api.login.controller;

import com.alibaba.fastjson.JSON;
import com.kevin.little.hotel.api.login.dto.LoginRequestDTO;
import com.kevin.little.hotel.api.login.service.LoginService;
import com.ruyuan.little.project.common.dto.CommonResponse;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 * @author wangyong
 */
@RestController
@RequestMapping(value = "/api/login")
public class LoginController {

  private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

  @Resource
  private LoginService loginService;


  /**
   * 登录请求
   *
   * @param loginRequestDTO 登录请求信息
   * @return 结果
   */
  @PostMapping(value = "/wxLogin")
  public CommonResponse wxLogin(@RequestBody LoginRequestDTO loginRequestDTO) {
    logger.info("login success user info:{} ", JSON.toJSONString(loginRequestDTO));

    // 第一次登陆下发优惠券
    loginService.firstLoginDistributeCoupon(loginRequestDTO);

    return CommonResponse.success();
  }

  /**
   * 重置登录状态 TODO 方便测试使用
   *
   * @param phoneNumber 用户手机号
   * @return 结果
   */
  @GetMapping(value = "/resetLoginStatus")
  public CommonResponse resetFirstLoginStatus(@RequestParam(value = "phoneNumber") String phoneNumber) {
    logger.info("reset user first login status phoneNumber:{}", phoneNumber);
    loginService.resetFirstLoginStatus(phoneNumber);
    return CommonResponse.success();
  }


}
