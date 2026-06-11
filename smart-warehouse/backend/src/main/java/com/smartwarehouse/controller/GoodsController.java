package com.smartwarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.Goods;
import com.smartwarehouse.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    @GetMapping("/list")
    public R<Page<Goods>> list(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(required = false) String keyword,
                                @RequestParam(required = false) Long categoryId) {
        Page<Goods> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Goods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Goods::getDeleted, 0);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Goods::getName, keyword).or().like(Goods::getCode, keyword);
        }
        if (categoryId != null) {
            wrapper.eq(Goods::getCategoryId, categoryId);
        }
        wrapper.orderByDesc(Goods::getCreateTime);
        Page<Goods> result = goodsMapper.selectPage(pageParam, wrapper);
        return R.ok(result).total(result.getTotal());
    }
    
    @GetMapping("/{id}")
    public R<Goods> getById(@PathVariable Long id) {
        Goods goods = goodsMapper.selectById(id);
        return R.ok(goods);
    }
    
    @PostMapping("/add")
    public R<Void> add(@RequestBody Goods goods) {
        goods.setDeleted(0);
        goodsMapper.insert(goods);
        return R.ok();
    }
    
    @PutMapping("/update")
    public R<Void> update(@RequestBody Goods goods) {
        goodsMapper.updateById(goods);
        return R.ok();
    }
    
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        Goods goods = new Goods();
        goods.setId(id);
        goods.setDeleted(1);
        goodsMapper.updateById(goods);
        return R.ok();
    }
}