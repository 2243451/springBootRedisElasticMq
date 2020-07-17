package com.lyf.controller;

import com.lyf.annotation.CheckNull;
import com.lyf.domain.User;
import com.lyf.util.Result;
import com.lyf.model.ResultInfo;
import com.lyf.domain.query.UserQuery;
import com.lyf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("save")
    @CheckNull({"name","age"})
    public ResultInfo save(User user){
        userService.save(user);
        return Result.success();
    }

    @RequestMapping("query/{id}")
    public ResultInfo query (@PathVariable Integer id){
        return Result.success(userService.queryById(id));
    }

    @RequestMapping("update")
    @CheckNull({"id"})
    @CacheEvict(value = "userCache", allEntries = true)
    public ResultInfo update (User user){
        userService.update(user);
        return Result.success();
    }

    @RequestMapping("list")
    @Cacheable(value = "userCache")
    public ResultInfo list (UserQuery query){
        return Result.success(userService.queryByParams(query));
    }

    @RequestMapping("query2/{id}")
    @CheckNull({"id"})
    public ResultInfo query2 (@PathVariable Integer id){
        return Result.success(userService.queryUser(id));
    }

    @RequestMapping("query3/{id}")
    @CheckNull({"id"})
    public ResultInfo query3 (@PathVariable Integer id){
        return Result.success(userService.queryUser2(id));
    }

}
