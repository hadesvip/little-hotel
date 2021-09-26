package com.kevin.little.hotel.common.consts;

public class EnumDict {

    private String name;
    private Integer code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public EnumDict(String name, Integer code) {
        this.name = name;
        this.code = code;
    }
}
