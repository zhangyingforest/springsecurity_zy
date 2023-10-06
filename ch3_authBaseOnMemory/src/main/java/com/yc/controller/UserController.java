package com.yc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api")   //用户必须登录才能访问
public class UserController {

    @GetMapping("hello")
    public String hello() {
        return "hello, user";
    }

}
