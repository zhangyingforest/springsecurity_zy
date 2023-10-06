package com.yc.config;

import com.yc.authentication.SecurityAuthenticationFailureHandler;
import com.yc.authentication.SecurityAuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @program: springsecurity_zy
 * @description: 此版本升级为自己配置登录页面，而不用security提供的默认页面
 * @author: zy
 * @create: 2023-10-05 10:30
 */
@EnableWebSecurity
public class SecurityConfig2 extends WebSecurityConfigurerAdapter {
    /*   HttpSecurity 是一个链式调用方式，每个方法执行完成后都返回一个上下文.

     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()//返回了一个URL拦截注册器,通过调用它的方法( anyRequest(), antMatchers(),  regexMatchers() )来匹配系统的url,并指定安全策略
                .antMatchers( "/css/**","/images/**","/js/**","/**/*.jpg").permitAll()   //允许这些路径不被拦截,具体配置写前面
                .anyRequest().authenticated()    //通用规则 写后面

                .and()   //结束当前标签配置，回到 HttpSecurity对象，再继续配置下一项.
                .formLogin()    // formLogin()和httpBasic()两个方法用于不同的配置方案
                .loginPage("/myLogin.html")   //登录页
                .loginProcessingUrl("/login")  // 默认处理请求  ->升级： 如何使用数据库模型
                .successHandler(new SecurityAuthenticationSuccessHandler())
                .failureHandler(new SecurityAuthenticationFailureHandler())
                .permitAll()  // 使登录页不设限访问

                .and()
                .csrf()   //spring security提供的跨站请求伪造防护功能，默认开启csrf().  这里调用了.disable()关闭csrf功能
                .disable();

    }

}
