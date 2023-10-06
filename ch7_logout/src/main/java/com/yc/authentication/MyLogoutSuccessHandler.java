package com.yc.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: springsecurity_zy
 * @description: 退出处理
 * @author: zy
 * @create: 2023-10-06 14:37
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (request.getHeader("accept").contains("application/json")) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\"error_code\":\"0\", \"message\":\"成功退出登录系统\"}");
        } else {
            response.sendRedirect("/index.html");
        }
    }
}
