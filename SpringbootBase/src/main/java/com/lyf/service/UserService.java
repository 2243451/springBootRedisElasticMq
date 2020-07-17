package com.lyf.service;

import com.lyf.annotation.LogTrace;
import com.lyf.base.BaseService;
import com.lyf.dao.UserMapper;
import com.lyf.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = {"userCache"})
public class UserService extends BaseService<User> {

    @Autowired
    private UserMapper userMapper;

    @Cacheable
    @LogTrace
    public User queryUser (Integer id){
        return userMapper.queryById(id);
    }

    @Cacheable(value = "defaultCache",key = "#id")
    public User queryUser2 (Integer id){
        return userMapper.queryById(id);
    }

}
