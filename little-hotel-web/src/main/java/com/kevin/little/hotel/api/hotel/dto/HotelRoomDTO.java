package com.kevin.little.hotel.api.hotel.dto;

import com.alibaba.fastjson.JSON;
import java.math.BigDecimal;
import java.util.List;

/**
 * 酒店房间详情
 *
 * @author wangyong
 */
public class HotelRoomDTO {

  /**
   * 房间ID
   */
  private Long id;

  /**
   * 房间名称
   */
  private String title;

  /**
   * 店铺id
   */
  private Long pCate;

  /**
   * 商品图片
   */
  private String thumbUrl;

  /**
   * 房间详细信息
   */
  private RoomDescriptionDTO roomDescription;

  /**
   * 房间图片信息
   */
  private List<RoomPictureDTO> goodsBanner;

  /**
   * 参考价格
   */
  private BigDecimal marketPrice;

  /**
   * 实际价格
   */
  private BigDecimal productPrice;

  /**
   * 商品的数量
   */
  private Integer total;

  private Integer totalCnf;

  /**
   * 创建时间 unix时间
   */
  private Long createTime;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getpCate() {
    return pCate;
  }

  public void setpCate(Long pCate) {
    this.pCate = pCate;
  }

  public String getThumbUrl() {
    return thumbUrl;
  }

  public void setThumbUrl(String thumbUrl) {
    this.thumbUrl = thumbUrl;
  }

  public RoomDescriptionDTO getRoomDescription() {
    return roomDescription;
  }

  public void setRoomDescription(RoomDescriptionDTO roomDescription) {
    this.roomDescription = roomDescription;
  }

  public List<RoomPictureDTO> getGoodsBanner() {
    return goodsBanner;
  }

  public void setGoodsBanner(
      List<RoomPictureDTO> goodsBanner) {
    this.goodsBanner = goodsBanner;
  }

  public BigDecimal getMarketPrice() {
    return marketPrice;
  }

  public void setMarketPrice(BigDecimal marketPrice) {
    this.marketPrice = marketPrice;
  }

  public BigDecimal getProductPrice() {
    return productPrice;
  }

  public void setProductPrice(BigDecimal productPrice) {
    this.productPrice = productPrice;
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Integer getTotalCnf() {
    return totalCnf;
  }

  public void setTotalCnf(Integer totalCnf) {
    this.totalCnf = totalCnf;
  }

  public Long getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}
