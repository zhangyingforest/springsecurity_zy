package com.yc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.com.com.yc.mapper")
public class Ch5_captchaApp {

    public static void main(String[] args) {
        SpringApplication.run(Ch5_captchaApp.class, args);
    }

}
