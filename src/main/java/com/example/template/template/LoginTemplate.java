package com.example.template.template;

import com.example.template.login.dto.UserInfoDto;
import lombok.extern.slf4j.Slf4j;

/**
 * 登录模板
 */
@Slf4j
public abstract class LoginTemplate implements LoginAdapter{
    protected UserInfoDto userInfoDto;
    protected Integer loginType;

    @Override
    public UserInfoDto userLogin(String loginForm) {
        // 初始化登录参数
        this.initialize(loginForm);
        // 登录前日志记录
        this.logBefore();
        // 自定义登录
        UserInfoDto userLoginInfo = this.login();
        // 黑名单校验
        this.isBlacklist();
        // 异地登录风控校验
        this.risk();
        // 用户权限校验
        this.checkUserAuth();
        // 构建登录用户响应数据
        this.buildUserInfo(userLoginInfo);
        // 记录登录信息
        this.logAfter();
        // 登录完成
        return userInfoDto;
    }

    protected abstract void initialize(String loginFrom);
    protected abstract UserInfoDto login();

    private void logAfter(){
        log.info("logAfter . . . . . ");
    }
    private void logBefore(){
        log.info("logBefore . . . . . .");
    }

    private void buildUserInfo(UserInfoDto userLoginInfo){
        this.userInfoDto = userLoginInfo;
        log.info("buildUserInfo:{}",userLoginInfo);
    }

    private void checkUserAuth(){
        log.info("checkUserAuth . . . . . ");
    }

    private void risk(){
        log.info("risk . . . . .");
    }

    private void isBlacklist(){
        log.info("isBlacklist . . . . . ");
    }
}
