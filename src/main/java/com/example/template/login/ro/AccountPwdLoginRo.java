package com.example.template.login.ro;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户秘密登录请求体
 */
@Data
public class AccountPwdLoginRo implements Serializable {
    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;
}
