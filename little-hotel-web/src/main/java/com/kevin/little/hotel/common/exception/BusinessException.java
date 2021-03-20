package com.kevin.little.hotel.common.exception;

/**
 * 业务异常
 *
 * @author wangyong
 */
public class BusinessException extends RuntimeException {

  public BusinessException(String message) {
    super(message);
  }
}
