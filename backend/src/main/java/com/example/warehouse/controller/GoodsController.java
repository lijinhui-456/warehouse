package com.example.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.warehouse.common.Result;
import com.example.warehouse.entity.Goods;
import com.example.warehouse.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/list")
    public Result list() {
        return Result.success(goodsService.list());
    }


    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer current,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) Long categoryId) {
        Page<Goods> page = new Page<>(current, size);
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            wrapper.like("name", name);
        }
        if (categoryId != null) {
            wrapper.eq("category_id", categoryId);
        }
        wrapper.orderByDesc("id");
        return Result.success(goodsService.page(page, wrapper));
    }


    @PostMapping
    public Result add(@RequestBody Goods goods) {
        goodsService.save(goods);
        return Result.success();
    }


    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @RequestBody Goods goods) {
        goods.setId(id);
        goodsService.updateById(goods);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id") Long id) {
        goodsService.removeById(id);
        return Result.success();
    }
}
