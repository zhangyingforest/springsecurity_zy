package com.yc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/api")   //要有管理员权限 才能访问
public class AdminController {

    @GetMapping("hello")
    public String hello() {
        return "hello, admin";
    }

}
