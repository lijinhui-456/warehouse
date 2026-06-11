package com.smartwarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.Inventory;
import com.smartwarehouse.mapper.InventoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @GetMapping("/list")
    public R<Page<Inventory>> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size,
                                    @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Long warehouseId) {
        Page<Inventory> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getDeleted, 0);
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like(Inventory::getGoodsName, keyword).or().like(Inventory::getGoodsCode, keyword);
        }
        if (warehouseId != null) {
            wrapper.eq(Inventory::getWarehouseId, warehouseId);
        }
        wrapper.orderByDesc(Inventory::getUpdateTime);
        Page<Inventory> result = inventoryMapper.selectPage(pageParam, wrapper);
        return R.ok(result).total(result.getTotal());
    }
    
    @GetMapping("/alert")
    public R<Page<Inventory>> alert(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer size) {
        Page<Inventory> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Inventory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Inventory::getDeleted, 0);
        wrapper.apply("quantity < min_quantity");
        wrapper.orderByAsc(Inventory::getQuantity);
        Page<Inventory> result = inventoryMapper.selectPage(pageParam, wrapper);
        return R.ok(result).total(result.getTotal());
    }
}