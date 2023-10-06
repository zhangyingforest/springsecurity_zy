在ch2_form的基础上完成 自定义的表单登录，增加验证码 显示 功能. 

注意点:
方案有很多，这里采用一个依赖完成. 
1. 引入了 验证码 依赖
     <dependency>
                  <groupId>com.github.penggle</groupId>
                  <artifactId>kaptcha</artifactId>
                  <version>2.3.2</version>
     </dependency>
   加入验证码配置:
   //验证码配置
   @Configuration
   public class CaptchaConfig {
   
       @Bean
       public Producer captcha() {
           // 配置图形验证码的基本参数
           Properties properties = new Properties();
           // 图片宽度
           properties.setProperty("kaptcha.image.width", "150");
           // 图片长度
           properties.setProperty("kaptcha.image.height", "50");
           // 字符集
           properties.setProperty("kaptcha.textproducer.char.string", "0123456789");
           // 字符长度
           properties.setProperty("kaptcha.textproducer.char.length", "4");
           Config config = new Config(properties);
           // 使用默认的图形验证码实现，当然也可以自定义实现
           DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
           defaultKaptcha.setConfig(config);
           return defaultKaptcha;
       }
   
   }
   加入controller
   @Controller
   public class CaptchaController {
       @Autowired
       private Producer captchaProducer;
       @GetMapping("/captcha.jpg")
       public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
           // 设置内容类型
           response.setContentType("image/jpeg");
           // 创建验证码文本
           String capText = captchaProducer.createText();
           // 将验证码文本设置到session
           request.getSession().setAttribute("captcha", capText);
           // 创建验证码图片
           BufferedImage bi = captchaProducer.createImage(capText);
           // 获取响应输出流
           ServletOutputStream out = response.getOutputStream();
           // 将图片验证码数据写到响应输出流
           ImageIO.write(bi, "jpg", out);
           // 推送并关闭响应输出流
           try {
               out.flush();
           } finally {
               out.close();
           }
       }
   }
   在前端页面加入链接:
   <img src="/captcha.jpg" alt="captcha" height="50px" width="150px" style="margin-left: 20px;">
   
   ===============================================
   以上完成后，请测试验证码是否可以正常显示出来. 
   
2. 加入验证码校验部分:   先请大家看成品，调试后再分析. 
   以前的做法:  session.getAttribute("captcha");
              request.getParameter("captcha");
              然后判断是否equals()即可

  在spring security中要考虑问题: 1. 要获取 标准验证码  -> 要找session -》 要有HttpServletRequest
                               2. 验证码在哪里进行比较   -> Provider
                               3. 这个比较用的Provider在哪里调用呢? -> 
                                    ProviderManager中保存有多个Provider,循环每一个provider, 调用它的 authenticate()完成额外验证. 
                                    另外思考一下: 在登录认证授权下，还会有更多种验证方案，所以利用provider可以很容易的追加新的验证方案了. 
                               4. ProviderManager又是谁调用呢? UsernamePasswordAuthenticationFilter，它和别的过滤器形成过滤器链
                               
   步骤一: 
   1. 创建一个  AuthenticationDetailsSource  对象, 通过它生成  WebAuthenticationDetails 对象
        @Component
        public class MyWebAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {
            @Override
            public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
                return new MyWebAuthenticationDetails(request);
            }
        }
   2. 创建一个  WebAuthenticationDetails 完成验证码信息的提取
        public class MyWebAuthenticationDetails extends WebAuthenticationDetails {      
            private String imageCode;
            private String savedImageCode;
            public String getImageCode() {
                return imageCode;
            }
            public String getSavedImageCode() {
                return savedImageCode;
            }
            // 补充用户提交的验证码和session保存的验证码
            public MyWebAuthenticationDetails(HttpServletRequest request) {
                super(request);
                this.imageCode = request.getParameter("captcha");
                HttpSession session = request.getSession();
                this.savedImageCode = (String) session.getAttribute("captcha");
                if (!StringUtils.isEmpty(this.savedImageCode)) {
                    // 随手清除验证码，不管是失败还是成功，所以客户端应在登录失败时刷新验证码
                    session.removeAttribute("captcha");
                }
            }
        }
   3. 创建  DaoAuthenticationProvider  对象，并托管起来. ,在它里面提供验证校验的方法
      @Component
      public class MyAuthenticationProvider extends DaoAuthenticationProvider {
          // 通过构造方法注入  UserDetailService  和  PasswordEncoder
          public MyAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
              this.setUserDetailsService(userDetailsService);
              this.setPasswordEncoder(passwordEncoder);
          }
          @Override
          protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) throws AuthenticationException {
              //获取详细信息
              MyWebAuthenticationDetails details = (MyWebAuthenticationDetails) usernamePasswordAuthenticationToken.getDetails();
              String imageCode = details.getImageCode();
              String savedImageCode = details.getSavedImageCode();
              // 检验图形验证码
              if (StringUtils.isEmpty(imageCode) || StringUtils.isEmpty(savedImageCode) || !imageCode.equals(savedImageCode)) {
                  //验证码不正确，失败.
                  throw new VerificationCodeException();
              }
              //调用父类方法完成密码验证.
              super.additionalAuthenticationChecks(userDetails, usernamePasswordAuthenticationToken);
          }
      }
   4. 在 SecurityConfig 类中完成装配过程. 
      
    


  更深入: spring security流程
      spring security大体上是由一堆Filter（所以才能在spring mvc前拦截请求）实现的，
      Filter有几个，登出Filter（LogoutFilter），用户名密码验证Filter（UsernamePasswordAuthenticationFilter）之类的，
      Filter再交由其他组件完成细分的功能，
      例如最常用的UsernamePasswordAuthenticationFilter会持有一个AuthenticationManager引用，AuthenticationManager顾名思义，验证管理器，
      负责验证的，但AuthenticationManager本身并不做具体的验证工作，AuthenticationManager持有一个AuthenticationProvider集合，
      AuthenticationProvider才是做验证工作的组件，  