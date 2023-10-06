package com.yc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.com.yc.mapper")
public class Ch7_logout {

    public static void main(String[] args) {
        SpringApplication.run(Ch7_logout.class, args);
    }

}
