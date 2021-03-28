package com.kevin.little.hotel.api.hotel.dto;

import com.alibaba.fastjson.JSON;

/**
 * 房间图片信息
 *
 * @author wangyong
 */
public class RoomPictureDTO {

  /**
   * 图片id
   */
  private Integer id;

  /**
   * 图片地址
   */
  private String url;

  private String src;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSrc() {
    return src;
  }

  public void setSrc(String src) {
    this.src = src;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
