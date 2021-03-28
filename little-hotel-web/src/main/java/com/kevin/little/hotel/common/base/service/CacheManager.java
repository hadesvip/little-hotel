package com.kevin.little.hotel.common.base.service;

import java.util.concurrent.ExecutionException;

/**
 * 缓存管理
 *
 * @author wangyong
 */
public interface CacheManager<Serializable, T> {

  /**
   * 获取缓存
   */
  T getByCacheKey(Serializable cacheKey);

  /**
   * 更新缓存
   */
  void update(T update);

  /**
   * 设置缓存
   */
  void put(Serializable cacheKey, T cacheValue);


}
