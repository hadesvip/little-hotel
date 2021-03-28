package com.kevin.little.hotel.admin.service;

import com.kevin.little.hotel.admin.dto.AdminHotelRoomDTO;
import com.ruyuan.little.project.common.dto.CommonResponse;

/**
 * @author wangyong
 */
public interface AdminRoomService {


  /**
   * 添加房间
   */
  CommonResponse add(AdminHotelRoomDTO adminHotelRoom);

  /**
   * 更新商品信息
   */
  CommonResponse update(AdminHotelRoomDTO adminHotelRoom);

}
