package com.lyf.dao;

import com.lyf.domain.elastic.GoodsDoc;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

@Mapper
public interface ElasticRepository extends ElasticsearchRepository<GoodsDoc, Long> {
    /**
     * 根据价格区间查询
     * @param price1
     * @param price2
     * @return
     */
    List<GoodsDoc> findByPriceBetween(double price1, double price2);
}
