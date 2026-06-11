package com.smartwarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.Warehouse;
import com.smartwarehouse.mapper.WarehouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {
    
    @Autowired
    private WarehouseMapper warehouseMapper;
    
    @GetMapping("/list")
    public R<Page<Warehouse>> list(@RequestParam(defaultValue = "1") Integer page,
                                    @RequestParam(defaultValue = "10") Integer size) {
        Page<Warehouse> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Warehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Warehouse::getDeleted, 0);
        wrapper.orderByDesc(Warehouse::getCreateTime);
        Page<Warehouse> result = warehouseMapper.selectPage(pageParam, wrapper);
        return R.ok(result).total(result.getTotal());
    }
    
    @GetMapping("/all")
    public R<java.util.List<Warehouse>> all() {
        LambdaQueryWrapper<Warehouse> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Warehouse::getDeleted, 0);
        wrapper.eq(Warehouse::getStatus, 1);
        return R.ok(warehouseMapper.selectList(wrapper));
    }
    
    @PostMapping("/add")
    public R<Void> add(@RequestBody Warehouse warehouse) {
        warehouse.setDeleted(0);
        warehouseMapper.insert(warehouse);
        return R.ok();
    }
    
    @PutMapping("/update")
    public R<Void> update(@RequestBody Warehouse warehouse) {
        warehouseMapper.updateById(warehouse);
        return R.ok();
    }
    
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable Long id) {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(id);
        warehouse.setDeleted(1);
        warehouseMapper.updateById(warehouse);
        return R.ok();
    }
}