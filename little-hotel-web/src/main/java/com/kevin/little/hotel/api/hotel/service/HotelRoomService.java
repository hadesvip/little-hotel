package com.kevin.little.hotel.api.hotel.service;

import com.kevin.little.hotel.api.hotel.dto.HotelRoomDTO;
import com.ruyuan.little.project.common.dto.CommonResponse;

/**
 * 酒店房间服务
 *
 * @author wangyong
 */
public interface HotelRoomService {


  /**
   * 根据ID获取酒店详情
   *
   * @param id          酒店ID
   * @param phoneNumber 手机号码
   * @return 酒店详情
   */
  CommonResponse<HotelRoomDTO> queryHotelRoomById(Long id, String phoneNumber);




}
