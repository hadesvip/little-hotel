package com.kevin.little.hotel.api.coupon.service;

/**
 * 优惠券服务
 *
 * @author wangyong
 */
public interface CouponService {

  /**
   * 分发优惠券
   *
   * @param beId           小程序ID
   * @param userId         用户ID
   * @param couponConfigId 优惠券ID
   * @param validDay       优惠券有效天数
   * @param sourceOrderId  订单ID
   * @param phoneNumber    电话
   */
  void distributeCoupon(Integer beId, Integer userId, Integer couponConfigId, Integer validDay,
      Integer sourceOrderId, String phoneNumber);

}
