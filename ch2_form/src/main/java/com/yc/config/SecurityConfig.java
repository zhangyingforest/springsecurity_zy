package com.yc.config;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 *  WebSecurityConfigurerAdapter: 此类中已经默认声明了一些安全特性:
 *     1. 验证所有请求
 *     2. 允许用户使用表单登录且提提供了一个简易表单
 *     3. 允许用户使用http基本认证.
 */
//@EnableWebSecurity   //启用spring security配置  , 注意此注解中有@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and().formLogin()
                .and().httpBasic();
    }

}
