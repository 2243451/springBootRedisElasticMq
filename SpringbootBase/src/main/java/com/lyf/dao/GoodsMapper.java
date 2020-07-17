package com.lyf.dao;

import com.lyf.domain.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper {

    int save(Goods record);

    Goods queryById(Integer id);

    int update(Goods record);

    int delete(Goods record);

    int updateGoodsNum(Integer id);
}