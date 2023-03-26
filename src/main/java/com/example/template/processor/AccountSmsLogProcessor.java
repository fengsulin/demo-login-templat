package com.example.template.processor;

import com.example.template.login.dto.UserInfoDto;
import com.example.template.login.enums.LoginTypeEnum;
import com.example.template.login.ro.AccountSmsLoginRo;
import com.example.template.template.LoginTemplate;
import com.example.template.utils.JsonUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountSmsLogProcessor extends LoginTemplate {

    private AccountSmsLoginRo smsLoginRo;

    @Override
    public Integer getLoginType() {
        return LoginTypeEnum.AccountSms.getCode();
    }

    @Override
    protected void initialize(String loginFrom) {
        AccountSmsLoginRo smsLoginRo = JsonUtils.str2Bean(loginFrom, AccountSmsLoginRo.class);

        super.loginType = getLoginType();
        this.smsLoginRo = smsLoginRo;
    }

    @Override
    protected UserInfoDto login() {
        // 模拟短信验证码登录
        String accountByDB = "123";
        String smsIdByRedis = "UUID";
        String smsCodeByRedis = "123456";

        if (this.smsLoginRo.getAccount().equals(accountByDB) && this.smsLoginRo.getSmsId().equals(smsIdByRedis) &&
        this.smsLoginRo.getSmsCode().equals(smsCodeByRedis)){
            // 登录成功
            UserInfoDto userInfoDto = new UserInfoDto();
            userInfoDto.setUserId("0002");
            userInfoDto.setUsername(accountByDB);
            return userInfoDto;
        }else {
            throw new RuntimeException("验证码错误或账号不存在，登录失败");
        }
    }
}
