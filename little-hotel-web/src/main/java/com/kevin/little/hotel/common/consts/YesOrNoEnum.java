package com.kevin.little.hotel.common.consts;

/**
 * yes和no 枚举
 *
 * @author wangyong
 */
public enum YesOrNoEnum {
  /**
   * 是
   */
  NO(0, "是"),

  /**
   * 否
   */
  YES(1, "否");


  /**
   * code码
   */
  private Integer code;

  /**
   * 描述
   */
  private String desc;

  YesOrNoEnum(Integer code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(String desc) {
    this.desc = desc;
  }
}
