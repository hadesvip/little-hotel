package com.kevin.little.hotel.api.hotel.dto;

import com.alibaba.fastjson.JSON;

/**
 * 房间描述
 *
 * @author wangyong
 */
public class RoomDescriptionDTO {

  /**
   * 面积
   */
  private String area;

  /**
   * 宽高
   */
  private String bed;

  /**
   * 早餐的份数
   */
  private Integer breakfast;

  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public String getBed() {
    return bed;
  }

  public void setBed(String bed) {
    this.bed = bed;
  }

  public Integer getBreakfast() {
    return breakfast;
  }

  public void setBreakfast(Integer breakfast) {
    this.breakfast = breakfast;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
