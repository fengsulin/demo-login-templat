package com.example.template.template;

import com.example.template.login.dto.UserInfoDto;

public interface LoginAdapter {

    /**
     * 用户统一登陆接口
     * @param loginForm：登录请求体的json字符串
     * @return
     */
    UserInfoDto userLogin(String loginForm);

    /**
     * 获取登录类型
     * @return
     */
    Integer getLoginType();
}
