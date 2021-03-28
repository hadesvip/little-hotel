package com.kevin.little.hotel.admin.dto;

import com.alibaba.fastjson.JSON;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyong
 */
public class AdminHotelRoomDTO {

  /**
   * 用户手机号
   */
  private String phoneNumber;

  /**
   * 房间id
   */
  private Long id;

  /**
   * "id":"4009", "title":"豪华客房", "pcate":"1981", "thumb_url":"https://weapp-1303909892.file.myqcloud.com//image/20201221/6e222a7cc34f48db.jpg",
   * <p>
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
  private AdminRoomDescriptionDTO roomDescription;

  /**
   * 房间图片信息
   */
  private List<AdminRoomPictureDTO> goodsBannerList;

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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

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

  public AdminRoomDescriptionDTO getRoomDescription() {
    return roomDescription;
  }

  public void setRoomDescription(AdminRoomDescriptionDTO roomDescription) {
    this.roomDescription = roomDescription;
  }

  public List<AdminRoomPictureDTO> getGoodsBannerList() {
    return goodsBannerList;
  }

  public void setGoodsBannerList(
      List<AdminRoomPictureDTO> goodsBannerList) {
    this.goodsBannerList = goodsBannerList;
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
