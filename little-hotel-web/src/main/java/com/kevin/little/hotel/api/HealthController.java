package com.kevin.little.hotel.api;

import com.ruyuan.little.project.common.dto.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author wangyong
 */
@RestController
public class HealthController {


  @GetMapping("/")
  public CommonResponse health(){
    return CommonResponse.success();
  }

}
