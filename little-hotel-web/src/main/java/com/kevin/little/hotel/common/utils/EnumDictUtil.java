package com.kevin.little.hotel.common.utils;

import com.kevin.little.hotel.common.consts.BaseEnum;
import com.kevin.little.hotel.common.consts.EnumDict;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字典工具
 *
 * @author wangyong
 */
public final class EnumDictUtil {

    private EnumDictUtil() {
    }

    public static <T extends Enum<T> & BaseEnum<T>> List<EnumDict> getEnumDicts(Class<T> cls) {
        return EnumSet.allOf(cls).stream().map(et -> new EnumDict(et.getName(), et.getCode())).collect(Collectors.toList());
    }

}
