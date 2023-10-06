package com.yc.config;

import com.yc.authentication.MyLogoutSuccessHandler;
import com.yc.authentication.SecurityAuthenticationFailureHandler;
import com.yc.authentication.SecurityAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;


@EnableWebSecurity(debug=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //@Autowired
    private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> myWebAuthenticationDetailsSource;
    //@Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    public void setMyWebAuthenticationDetailsSource(AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> myWebAuthenticationDetailsSource) {
        this.myWebAuthenticationDetailsSource = myWebAuthenticationDetailsSource;
    }
    @Autowired
    @Lazy   //为了防止循环依赖
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 应用AuthenticationProvider
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers( "/css/**","/images/**","/js/**","/**/*.jpg").permitAll()   //允许这些路径不被拦截,具体配置写前面
                .antMatchers("/admin/api/**").hasRole("ADMIN")
                .antMatchers("/user/api/**").hasRole("USER")
                .antMatchers("/app/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .authenticationDetailsSource(  myWebAuthenticationDetailsSource   )
                .failureHandler(new SecurityAuthenticationFailureHandler())
                .successHandler(new SecurityAuthenticationSuccessHandler())
                .loginPage("/myLogin.html")
                .loginProcessingUrl("/login")   //指定映射路径
                .permitAll()
                .and()

                 //退出注解配置
                 .logout()
                //指定接受注解请求的路由
                .logoutUrl("/myLogout")
                //注销成功,重定向到此路径
                //.logoutSuccessUrl("/myLogin.html")
                .invalidateHttpSession(true)
                //.deleteCookies("cookie名字")   //还可以删除一些cookie
                //注解成功后的处理方式，比如以json形式输出，
                .logoutSuccessHandler(   new MyLogoutSuccessHandler() )
                .and()

                .csrf().disable();
    }

   // @Bean
   // public PasswordEncoder passwordEncoder() {
    //    return NoOpPasswordEncoder.getInstance();
   // }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder( );
    }

    public static void main(String[] args) {
        PasswordEncoder pe=new BCryptPasswordEncoder( );
        //生成 BCrypt密码
        String pw=pe.encode("123");
        System.out.println(  pw  );
        //$2a$10$J2E.ks64sKYO5YIb6595k.MDsjen4yxiD7kfPZhQn8LZle3dlTPCy
        //$2a 版本
        //$10 10次迭代
        //$J2E.ks64sKYO5YIb6595k 加盐
        //.MDsjen4yxiD7kfPZhQn8LZle3dlTPCy   真正的散列值

        //假设用户输入了  x  判断BCrypt密码
        String x="123";
        System.out.println( pe.matches(  x,  pw ) );
    }

}
