package com.example.template.template;

import com.example.template.login.dto.UserInfoDto;
import com.example.template.template.LoginAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 登录策略工厂类
 */
@Component
@Slf4j
public class LoginFactory implements ApplicationContextAware {

    private Map<Integer,LoginAdapter> processMap = new ConcurrentHashMap(3); // 默认有三种策略

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, LoginAdapter> tempMap = applicationContext.getBeansOfType(LoginAdapter.class);
        for (LoginAdapter value : tempMap.values()) {
            processMap.put(value.getLoginType(),value);
        }
    }

    public UserInfoDto userLogin(Integer loginType,String loginFrom){
        LoginAdapter loginAdapter = processMap.get(loginType);
        if (loginAdapter != null){
            return loginAdapter.userLogin(loginFrom);
        }else {
           log.error("{}：策略未找到",loginType);
           return null;
        }
    }
}
