package com.example.template.login.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户基本信息类
 */
@Data
public class UserInfoDto implements Serializable {
    /**
     * 用户名
     */
    private String username;

    /**
     * 用户id
     */
    private String userId;
}
