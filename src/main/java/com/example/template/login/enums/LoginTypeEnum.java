package com.example.template.login.enums;

import lombok.Getter;

import java.util.Objects;

@Getter
public enum LoginTypeEnum {
    AccountPwd(0,"用户名密码登录"),
    AccountSms(1,"短信验证码登录");
    private final Integer code;
    private final String typeName;

    LoginTypeEnum(Integer code, String typeName) {
        this.code = code;
        this.typeName = typeName;
    }

    /**
     * 根据code获取typeName
     * @param code
     * @return
     */
    public static String getTypeNameByCode(Integer code){
        if (Objects.nonNull(code)){
            for (LoginTypeEnum value : values()) {
                if (value.getCode().equals(code)){
                    return value.getTypeName();
                }
            }
        }
        return null;
    }
}
