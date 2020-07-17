package com.lyf.controller;

import com.lyf.domain.User;
import com.lyf.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("save")
    public String set (String key, String val){
        redisService.set(key, val);
        return "OK";
    }

    @GetMapping("setUser")
    public String setUser (User user){
        redisService.setUser(user);
        return "OK";
    }

    @GetMapping("get")
    public Object get (String key){
        return redisService.get(key);
    }

}
