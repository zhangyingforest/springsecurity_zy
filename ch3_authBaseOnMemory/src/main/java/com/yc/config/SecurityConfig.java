package com.yc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // Ant模式的URL匹配, ？表示单个字符, *匹配任意个.    **匹配更多目录
                .antMatchers("/admin/api/**").hasRole("ADMIN")   //要有ADMIN角色
                .antMatchers("/user/api/**").hasRole("USER")     //要有USER角色.
                .antMatchers("/app/api/**").permitAll()  //公开访问
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }

    /**
     * InMemoryUserDetailsManager是UserDetailsService接口的一个实现， 这将用户数据源存在内存里。
     * @return
     */
    @Override
    @Bean   //基于内存的多用户支持..
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("user").password("123").roles("USER").build());
        manager.createUser(User.withUsername("admin").password("123").roles("USER", "ADMIN").build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
