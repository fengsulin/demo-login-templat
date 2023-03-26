package com.example.template.utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * request请求处理工具类
 */
@Slf4j
public class RequestUtils {

    /**
     * 读取请求体
     * @param request
     * @return
     */
    public static String getRequestJson(HttpServletRequest request){
        BufferedReader streamReader = null;
        try {
            streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null){
                sb.append(line);
            }
            System.out.println(sb.toString());
            return sb.toString();
        } catch (IOException e) {
            log.error("request请求体获取异常：{}",e);
        }finally {
            if (streamReader != null){
                try {
                    streamReader.close();
                } catch (IOException e) {
                    log.error("request请求体读取流关闭异常：{}",e);
                }
            }
        }
        return "";
    }
}
