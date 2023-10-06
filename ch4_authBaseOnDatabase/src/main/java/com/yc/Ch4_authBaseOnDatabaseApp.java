package com.yc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.com.com.com.com.yc.mapper")
public class Ch4_authBaseOnDatabaseApp {

    public static void main(String[] args) {
        SpringApplication.run(Ch4_authBaseOnDatabaseApp.class, args);
    }

}
