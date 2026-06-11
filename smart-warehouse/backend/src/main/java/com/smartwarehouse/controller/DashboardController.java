package com.smartwarehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.*;
import com.smartwarehouse.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    
    @Autowired
    private GoodsMapper goodsMapper;
    
    @Autowired
    private InventoryMapper inventoryMapper;
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private WarehouseMapper warehouseMapper;
    
    @GetMapping("/stats")
    public R<Map<String, Object>> stats() {
        Map<String, Object> data = new HashMap<>();
        
        // 商品总数
        LambdaQueryWrapper<Goods> goodsWrapper = new LambdaQueryWrapper<>();
        goodsWrapper.eq(Goods::getDeleted, 0);
        data.put("goodsCount", goodsMapper.selectCount(goodsWrapper));
        
        // 库存总量
        LambdaQueryWrapper<Inventory> inventoryWrapper = new LambdaQueryWrapper<>();
        inventoryWrapper.eq(Inventory::getDeleted, 0);
        List<Inventory> inventories = inventoryMapper.selectList(inventoryWrapper);
        double totalQuantity = inventories.stream()
                .mapToDouble(i -> i.getQuantity() != null ? i.getQuantity().doubleValue() : 0)
                .sum();
        data.put("inventoryQuantity", totalQuantity);
        
        // 预警数量
        LambdaQueryWrapper<Inventory> alertWrapper = new LambdaQueryWrapper<>();
        alertWrapper.eq(Inventory::getDeleted, 0);
        alertWrapper.apply("quantity < min_quantity");
        data.put("alertCount", inventoryMapper.selectCount(alertWrapper));
        
        // 订单总数
        LambdaQueryWrapper<Order> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Order::getDeleted, 0);
        data.put("orderCount", orderMapper.selectCount(orderWrapper));
        
        // 仓库数量
        LambdaQueryWrapper<Warehouse> warehouseWrapper = new LambdaQueryWrapper<>();
        warehouseWrapper.eq(Warehouse::getDeleted, 0);
        data.put("warehouseCount", warehouseMapper.selectCount(warehouseWrapper));
        
        return R.ok(data);
    }
    
    @GetMapping("/charts")
    public R<Map<String, Object>> charts() {
        Map<String, Object> data = new HashMap<>();
        
        // 库存分布
        List<Map<String, Object>> inventoryDist = new ArrayList<>();
        String[] categories = {"电子产品", "服装鞋帽", "食品饮料", "家居用品", "其他"};
        int[] values = {35, 25, 20, 12, 8};
        for (int i = 0; i < categories.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", categories[i]);
            item.put("value", values[i]);
            inventoryDist.add(item);
        }
        data.put("inventoryDistribution", inventoryDist);
        
        // 订单趋势
        List<Map<String, Object>> orderTrend = new ArrayList<>();
        String[] months = {"1月", "2月", "3月", "4月", "5月", "6月"};
        int[] orders = {120, 150, 180, 160, 200, 220};
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", months[i]);
            item.put("count", orders[i]);
            orderTrend.add(item);
        }
        data.put("orderTrend", orderTrend);
        
        return R.ok(data);
    }
}