目标:  
1. 完成注销登录

   默认情况下，就已经提供了注销的配置. 访问路径:  /logout  ，操作完成后跳到 默认的login.html页面
   
  但在前后端分离的项目中要求返回一个JSON字符串. 所以要通过配置 Handler来实现. 
  
  
  
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

  
  这里为了灵活处理退出，定义了一个 Handler
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