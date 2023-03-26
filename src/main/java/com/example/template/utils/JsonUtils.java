package com.example.template.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json转换工具类
 */
public class JsonUtils {
    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T str2Bean(String jsonStr,Class<T> destClazz){
        try {
            return objectMapper.readValue(jsonStr, destClazz);
        } catch (JsonProcessingException e) {
            log.error("json字符转实体异常：{}",e);
        }
        return null;
    }

    public static String bean2Str(Object origObj){
        try {
            return origObj == null ? null : objectMapper.writeValueAsString(origObj);
        } catch (JsonProcessingException e) {
            log.error("实体类转字符串异常，{}",e);
        }
        return null;
    }
}
