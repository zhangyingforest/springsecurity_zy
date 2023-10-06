package com.yc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/api")  //公开访问
public class AppController {

    @GetMapping("hello")
    public String hello() {
        return "hello, app";
    }

}
