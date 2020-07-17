package com.lyf.controller;

import com.lyf.domain.elastic.GoodsDoc;
import com.lyf.util.Result;
import com.lyf.model.ResultInfo;
import com.lyf.service.ElasticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("es")
public class ElasticController {

    @Autowired
    private ElasticService elasticService;

    @RequestMapping("init")
    public ResultInfo init(Integer id){
        elasticService.init();
        return Result.success();
    }

    @RequestMapping("rm")
    public ResultInfo delIndex(){
        elasticService.delIndex();
        return Result.success();
    }

    @RequestMapping("alias")
    public ResultInfo alias(String index, String alias){
            elasticService.alias(index, alias);
            return Result.success();
    }

    @RequestMapping("add")
    public ResultInfo add(GoodsDoc goodsDoc){
        elasticService.add(goodsDoc);
        return Result.success();
    }

    @RequestMapping("addBatch")
    public ResultInfo addBatch(@RequestBody List<GoodsDoc> goodsDocList){
        elasticService.addBatch(goodsDocList);
        return Result.success();
    }

    @RequestMapping("del")
    public ResultInfo del(Long id){
        elasticService.del(id);
        return Result.success();
    }

    @RequestMapping("find")
    public ResultInfo find(Long id){
        return Result.success( elasticService.find(id));
    }

    @RequestMapping("all")
    public ResultInfo all(String name, @RequestParam(defaultValue = "0") int sort){
        return Result.success( elasticService.all(name, sort));
    }

    @RequestMapping("queryByPriceBetween")
    public ResultInfo queryByPriceBetween(Double price1, Double price2){
        return Result.success( elasticService.queryByPriceBetween(price1, price2));
    }

    @RequestMapping("page")
    public ResultInfo page(String name, String key,
                           @RequestParam(defaultValue = "1") Integer page,
                           @RequestParam(defaultValue = "10") Integer size,
                           @RequestParam(defaultValue = "0") int sort){
        return Result.success( elasticService.pageQuery(name, key, page, size, sort));
    }
}
