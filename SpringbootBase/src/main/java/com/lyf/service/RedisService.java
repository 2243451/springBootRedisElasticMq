package com.lyf.service;


import com.lyf.domain.User;
import com.lyf.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisUtil redisUtil;

    public String set (String key, String val){
        redisUtil.set(key, val);
        return "OK";
    }

    public String setUser (User user){
        redisUtil.set("user:"+user.toString(), user);
        return "OK";
    }

    public Object get (String key){
        return redisUtil.get(key);
    }


}
