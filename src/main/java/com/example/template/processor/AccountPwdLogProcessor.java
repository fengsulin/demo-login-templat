package com.example.template.processor;

import com.example.template.login.dto.UserInfoDto;
import com.example.template.login.enums.LoginTypeEnum;
import com.example.template.login.ro.AccountPwdLoginRo;
import com.example.template.template.LoginTemplate;
import com.example.template.utils.JsonUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountPwdLogProcessor extends LoginTemplate {
    private AccountPwdLoginRo pwdLoginRo;

    @Override
    protected void initialize(String loginFrom) {
        AccountPwdLoginRo loginRo = JsonUtils.str2Bean(loginFrom, AccountPwdLoginRo.class);
        // 对模板的登录参数赋值，可在其他校验方法中使用（可根据需求）
        super.loginType = LoginTypeEnum.AccountPwd.getCode();
        // 对当前上下文登录参数赋值，可以在下面的登录方法中使用
        this.pwdLoginRo = loginRo;
    }

    @Override
    protected UserInfoDto login() {
        // 模拟账号密码登录
        String accountByDB = "admin";
        String passwordByDB = "123456";

        // 这里只做简单的校验
        if (this.pwdLoginRo.getAccount().equals(accountByDB) && this.pwdLoginRo.getPassword().equals(passwordByDB)){
            // 登录成功
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setUserId("0001");
            userInfoDto.setUsername("admin");
            return userInfoDto;
        }else {
            throw new RuntimeException("账号密码错误或账号不存在，登录失败");
        }
    }

    @Override
    public Integer getLoginType() {
        return LoginTypeEnum.AccountPwd.getCode();
    }
}
