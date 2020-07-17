package com.lyf.service;

import com.lyf.dao.ElasticRepository;
import com.lyf.domain.elastic.GoodsDoc;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.AliasQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lyf.util.CollectionUtil.copyIterator;

@Service
public class ElasticService {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticRepository elasticRepository;

    public void init(){
        elasticsearchTemplate.createIndex(GoodsDoc.class);
        elasticsearchTemplate.putMapping(GoodsDoc.class);
    }

    public void delIndex(){
        elasticsearchTemplate.deleteIndex(GoodsDoc.class);
    }

    public void alias(String index, String alias){
        AliasQuery aliasQuery = new AliasQuery();
        aliasQuery.setIndexName(index);
        aliasQuery.setAliasName(alias);
        elasticsearchTemplate.addAlias(aliasQuery);
    }

    public void add(GoodsDoc goodsDoc){
        elasticRepository.save(goodsDoc);
    }

    public void addBatch(List<GoodsDoc> goodsDocList){
        elasticRepository.saveAll(goodsDocList);
    }

    public void del(Long id){
        elasticRepository.deleteById(id);
    }

    public GoodsDoc find(Long id){
        return elasticRepository.findById(id).get();
    }

    public List<GoodsDoc> all(String name, int sort){
        Direction direction = sort == 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
        Iterable<GoodsDoc> all = elasticRepository.findAll(Sort.by(direction,name));
        return copyIterator(all);
    }

    public List<GoodsDoc> queryByPriceBetween(Double price1, Double price2){
        return elasticRepository.findByPriceBetween(price1, price2);
    }

    // 高级查询
    // 分页+排序
    public Map pageQuery(String name, String value, Integer page, Integer size, int sort){
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        // 添加基本的分词查询
        queryBuilder.withQuery(QueryBuilders.termQuery(name, value));
        // 排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(sort==0 ? SortOrder.ASC : SortOrder.DESC));
        // 设置分页参数 es默认分页从0开始
        queryBuilder.withPageable(PageRequest.of(page-1, size));
        // 执行搜索，获取结果
        Page<GoodsDoc> search = elasticRepository.search(queryBuilder.build());
        Map result = new HashMap();
        result.put("total", search.getTotalElements());
        result.put("pages", search.getTotalPages());
        result.put("list", copyIterator(search.getContent()));
        return result;
    }

}
