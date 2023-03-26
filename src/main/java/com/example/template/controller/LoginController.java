package com.example.template.controller;

import com.example.template.login.dto.UserInfoDto;
import com.example.template.template.LoginFactory;
import com.example.template.utils.JsonUtils;
import com.example.template.utils.RequestUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Resource
    private LoginFactory loginFactory;

    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam("loginType") Integer loginType, HttpServletRequest request){
        UserInfoDto userInfoDto = loginFactory.userLogin(loginType, RequestUtils.getRequestJson(request));
        return JsonUtils.bean2Str(userInfoDto);
    }
}
