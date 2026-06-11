package com.smartwarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.Order;
import com.smartwarehouse.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @GetMapping("/list")
    public R<Page<Order>> list(@RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer size,
                                @RequestParam(required = false) Integer status) {
        Page<Order> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getDeleted, 0);
        if (status != null) {
            wrapper.eq(Order::getStatus, status);
        }
        wrapper.orderByDesc(Order::getCreateTime);
        Page<Order> result = orderMapper.selectPage(pageParam, wrapper);
        return R.ok(result).total(result.getTotal());
    }
    
    @GetMapping("/{id}")
    public R<Order> getById(@PathVariable Long id) {
        return R.ok(orderMapper.selectById(id));
    }
    
    @PutMapping("/status")
    public R<Void> updateStatus(@RequestBody Order order) {
        Order update = new Order();
        update.setId(order.getId());
        update.setStatus(order.getStatus());
        orderMapper.updateById(update);
        return R.ok();
    }
}