package com.example.template.login.ro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountSmsLoginRo implements Serializable {
    /**
     * 短信验证码
     */
    private String smsCode;

    /**
     * 短信验证码Id
     */
    private String smsId;

    /**
     * 手机号
     */
    private String account;
}
