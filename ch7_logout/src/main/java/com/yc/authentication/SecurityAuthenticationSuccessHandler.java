package com.yc.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//认证成功的处理
public class SecurityAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        //只有在请求头中加了   accept才会用json输出，所以请用 postman工具测试
        if (request.getHeader("accept").contains("application/json")) {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.write("{\"error_code\":\"0\", \"message\":\"欢迎登录系统\"}");
        } else {
            //没有加,则走默认输出
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
