package com.lyf.dao;

import com.lyf.base.BaseDao;
import com.lyf.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseDao<User> {
}