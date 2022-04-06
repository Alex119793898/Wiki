package com.caoliang.wiki.controller;

import com.caoliang.wiki.pojo.Test;
import com.caoliang.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class HelloController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public String hello(){
        return "hello spring!";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name){
        return "hello spring POST" + name + "!";
    }

    @GetMapping("/list")
    public List<Test> list(){
        return testService.list();
    }
}
