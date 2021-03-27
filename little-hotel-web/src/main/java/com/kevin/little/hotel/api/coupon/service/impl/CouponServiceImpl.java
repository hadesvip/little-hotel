package com.kevin.little.hotel.api.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.kevin.little.hotel.api.coupon.service.CouponService;
import com.kevin.little.hotel.common.utils.DateUtil;
import com.kevin.little.hotel.common.utils.DateUtil.DateFormatEnum;
import com.ruyuan.little.project.common.dto.CommonResponse;
import com.ruyuan.little.project.common.enums.LittleProjectTypeEnum;
import com.ruyuan.little.project.mysql.api.MysqlApi;
import com.ruyuan.little.project.mysql.dto.MysqlRequestDTO;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.time.DateUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 优惠券服务具体实现
 *
 * @author wangyong
 */
@Service
public class CouponServiceImpl implements CouponService {

  private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

  @Reference(version = "1.0.0",
      interfaceClass = MysqlApi.class,
      cluster = "failfast")
  private MysqlApi mysqlApi;


  @Override
  public void distributeCoupon(Integer beId, Integer userId, Integer couponConfigId,
      Integer validDay, Integer sourceOrderId, String phoneNumber) {
    MysqlRequestDTO mysqlRequestDTO = buildMysqlRequestDTO(beId,
        userId, couponConfigId, validDay, sourceOrderId, phoneNumber);
    logger.info("分发优惠券给用户,request：{}", JSON.toJSONString(mysqlRequestDTO));
    CommonResponse<Integer> response = mysqlApi.insert(mysqlRequestDTO);
    logger.info("分发优惠券给用户成功,response：{}", JSON.toJSONString(response));
  }

  /**
   * 构建mysql请求参数
   */
  private MysqlRequestDTO buildMysqlRequestDTO(Integer beId, Integer userId, Integer couponConfigId,
      Integer validDay, Integer sourceOrderId, String phoneNumber) {
    MysqlRequestDTO mysqlRequestDTO = new MysqlRequestDTO();
    mysqlRequestDTO.setSql("INSERT INTO t_coupon_user ("
        + " coupon_id,"
        + " beid,"
        + " uid,"
        + " begin_date,"
        + " end_date, "
        + " source_order_id "
        + ") "
        + "VALUES "
        + "("
        + " ?,"
        + " ?,"
        + " ?,"
        + " ?,"
        + " ?, "
        + " ? "
        + ")");
    List<Object> paramList = buildSaveCouponUserParam(beId, userId, couponConfigId, validDay,
        sourceOrderId);
    mysqlRequestDTO.setParams(paramList);
    mysqlRequestDTO.setPhoneNumber(phoneNumber);
    mysqlRequestDTO.setProjectTypeEnum(LittleProjectTypeEnum.ROCKETMQ);
    return mysqlRequestDTO;
  }

  /**
   * 构建保存优惠券优化参数
   */
  private List<Object> buildSaveCouponUserParam(Integer beId, Integer userId,
      Integer couponConfigId,
      Integer validDay, Integer sourceOrderId) {
    List<Object> paramList = Lists.newArrayList();
    paramList.add(couponConfigId);
    paramList.add(beId);
    paramList.add(userId);
    Date currDate = new Date();
    //开始时间
    paramList.add(DateUtil.getDateFormat(currDate, DateFormatEnum.Y_M_D_PATTERN.getPattern()));
    //结束时间
    paramList.add(
        DateUtil.getDateFormat(DateUtils.addDays(currDate, validDay),
            DateFormatEnum.Y_M_D_PATTERN.getPattern()));
    paramList.add(sourceOrderId);
    return paramList;
  }
}
