package com.java.wangyihan.util;

import android.arch.persistence.room.TypeConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public class Converter {
    @TypeConverter
    public static String fromMapToString(Map<String, Object> value) {
        return value == null ? "" : JSON.toJSONString(value);
    }

    @TypeConverter
    public static Map<String, Object> fromString(String value) {
        JSONObject jsonObject = JSON.parseObject(value);
        Map<String, Object> map = (Map<String, Object>)jsonObject;
        return value == null ? null : map;
    }

}
