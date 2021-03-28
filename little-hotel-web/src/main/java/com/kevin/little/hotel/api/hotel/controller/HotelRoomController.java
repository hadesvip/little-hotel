package com.kevin.little.hotel.api.hotel.controller;

import com.kevin.little.hotel.api.hotel.service.HotelRoomService;
import com.ruyuan.little.project.common.dto.CommonResponse;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 酒店房间
 *
 * @author wangyong
 */
@RestController
@RequestMapping(value = "/api/hotel")
public class HotelRoomController {

  private static final Logger logger = LoggerFactory.getLogger(HotelRoomController.class);

  @Resource
  private HotelRoomService hotelRoomService;


  /**
   * 根据酒店ID查询酒店详情
   */
  @GetMapping(value = "getRoomById")
  public CommonResponse getRoomById(@RequestParam(value = "id") Long id,
      @RequestParam(value = "phoneNumber") String phoneNumber) {
    logger.info("获取酒店房间详情,请求参数,id:{},phoneNumber:{}", id, phoneNumber);
    return hotelRoomService.queryHotelRoomById(id, phoneNumber);
  }


}
