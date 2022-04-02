package com.caoliang.wiki.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello spring!";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello spring POST" + name + "!";
    }
}
