package com.kevin.little.hotel.admin.controller;

import com.kevin.little.hotel.admin.dto.AdminHotelRoomDTO;
import com.kevin.little.hotel.admin.service.AdminRoomService;
import com.ruyuan.little.project.common.dto.CommonResponse;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangyong
 */
@RestController
@RequestMapping(value = "/admin/hotel/room")
public class AdminHotelRoomController {

  @Resource
  private AdminRoomService roomAdminService;

  @PostMapping(value = "/add")
  public CommonResponse add(@RequestBody AdminHotelRoomDTO adminHotelRoom) {
    return roomAdminService.add(adminHotelRoom);
  }

  @PostMapping(value = "/update")
  public CommonResponse update(@RequestBody AdminHotelRoomDTO adminHotelRoom) {
    return roomAdminService.update(adminHotelRoom);
  }


}
