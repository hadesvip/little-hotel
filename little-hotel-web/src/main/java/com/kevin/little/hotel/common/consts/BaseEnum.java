package com.kevin.little.hotel.common.consts;

/**
 * 基础枚举类
 * @author  wangyong
 * @param <T>
 */
public interface BaseEnum<T extends Enum<T> & BaseEnum<T>> {

    Integer getCode();

    String getName();

    static<T extends Enum<T> & BaseEnum<T> > T parseByCode(Class<T> cls, Integer code){
        for (T t : cls.getEnumConstants()){
            if (t.getCode().intValue() == code.intValue()){
                return t;
            }
        }
        return null;
    }


}
