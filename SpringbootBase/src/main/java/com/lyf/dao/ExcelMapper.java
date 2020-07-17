package com.lyf.dao;

import com.lyf.base.BaseDao;
import com.lyf.domain.excel.ExcelEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExcelMapper extends BaseDao<ExcelEntity> {

    List<ExcelEntity> all();
}