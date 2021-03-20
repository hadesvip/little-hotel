package com.kevin.little.hotel.api.login.service.impl;

import com.alibaba.fastjson.JSON;
import com.kevin.little.hotel.api.login.dto.LoginRequestDTO;
import com.kevin.little.hotel.api.login.service.LoginService;
import com.kevin.little.hotel.common.consts.YesOrNoEnum;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.ErrorCodeEnum;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.mysql.api.MysqlApi;
import com.ruyuan.little.project.mysql.dto.MysqlRequestDTO;
import com.ruyuan.little.project.redis.api.RedisApi;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * 登录服务具体实现
 *
 * @author wangyong
 */
@Service
public class LoginServiceImpl implements LoginService {

  private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

  @Reference(version = "1.0.0", interfaceClass = RedisApi.class, cluster = "failfast")
  private RedisApi redisApi;

  @Reference(version = "1.0.0",
      interfaceClass = MysqlApi.class,
      cluster = "failfast")
  private MysqlApi mysqlApi;


  @Resource
  @Qualifier(value = "loginMqProducer")
  private DefaultMQProducer loginMqProducer;

  @Value("${rocketmq.login.topic}")
  private String loginTopic;


  @Override
  public void firstLoginDistributeCoupon(LoginRequestDTO loginRequestDTO) {
    logger.info("");
  }

  @Override
  public void resetFirstLoginStatus(LoginRequestDTO loginRequestDTO) {
    this.updateFirstLoginStatus(loginRequestDTO.getPhoneNumber(), YesOrNoEnum.YES.getCode());
  }


  /**
   * 是否首次登录
   */
  private boolean isFirstLogin(LoginRequestDTO loginRequestDTO) {
    MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
    mysqlRequestDTO.setSql("select first_login_status from t_member where id=?");
    mysqlRequestDTO.setParams(Collections.singletonList(loginRequestDTO.getUserId()));
    mysqlRequestDTO.setPhoneNumber(loginRequestDTO.getPhoneNumber());
    mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
    logger.info("start query first login status param:{}", JSON.toJSONString(mysqlRequestDTO));
    CommonResponse<List<Map<String, Object>>> response = mysqlApi.query(mysqlRequestDTO);
    logger.info("end query first login status param:{}, response:{}",
        JSON.toJSONString(mysqlRequestDTO), JSON.toJSONString(response));
    if (Objects.equals(response.getCode(), ErrorCodeEnum.SUCCESS.getCode())
        && !CollectionUtils.isEmpty(response.getData())) {
      Map<String, Object> map = response.getData().get(0);
      return Objects.equals(Integer.valueOf(String.valueOf(map.get("first_login_status"))),
          YesOrNoEnum.YES.getCode());
    }
    return false;
  }

  /**
   * 更新第一次登陆的标志位
   *
   * @param phoneNumber 用户手机号
   * @param loginStatus 登录状态 {@link YesOrNoEnum}
   */
  private void updateFirstLoginStatus(String phoneNumber, Integer loginStatus) {
    MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
    mysqlRequestDTO
        .setSql("update t_member set first_login_status = ? WHERE beid = 1563 and mobile = ?");
    mysqlRequestDTO.setParams(Arrays.asList(loginStatus, phoneNumber));
    mysqlRequestDTO.setPhoneNumber(phoneNumber);
    mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
    logger.info("start query first login status param:{}", JSON.toJSONString(mysqlRequestDTO));
    CommonResponse<Integer> response = mysqlApi.update(mysqlRequestDTO);
    logger.info("end query first login status param:{}, response:{}",
        JSON.toJSONString(mysqlRequestDTO), JSON.toJSONString(response));
  }

  /**
   * 发送首次登陆消息
   *
   * @param loginRequestDTO 登陆请求信息
   */
  private void sendFirstLoginMessage(LoginRequestDTO loginRequestDTO) {
    // 场景一:性能提升  异步发送一个登录成功的消息到mq中
    Message message = new Message();
    message.setTopic(loginTopic);
    // 消息内容用户id
    message.setBody(JSON.toJSONString(loginRequestDTO).getBytes(StandardCharsets.UTF_8));
    try {
      logger.info("start send login success notify message");
      SendResult sendResult = loginMqProducer.send(message);
      logger.info("end send login success notify message, sendResult:{}",
          JSON.toJSONString(sendResult));
    } catch (Exception e) {
      logger.error("send login success notify message fail, error message:{}", e);
    }
  }

}
