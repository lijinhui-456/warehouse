package com.example.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.warehouse.common.Result;
import com.example.warehouse.entity.Category;
import com.example.warehouse.entity.Goods;
import com.example.warehouse.entity.Stock;
import com.example.warehouse.entity.StockRecord;
import com.example.warehouse.service.CategoryService;
import com.example.warehouse.service.GoodsService;
import com.example.warehouse.service.StockRecordService;
import com.example.warehouse.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockRecordService stockRecordService;

    @GetMapping("/stats")
    public Result stats() {
        Map<String, Object> data = new HashMap<>();

        long goodsCount = goodsService.count();
        long categoryCount = categoryService.count();
        long stockTotal = stockService.list().stream().mapToInt(Stock::getNum).sum();

        LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        QueryWrapper<StockRecord> todayWrapper = new QueryWrapper<>();
        todayWrapper.ge("created_at", todayStart);
        List<StockRecord> todayRecords = stockRecordService.list(todayWrapper);
        long todayIn = todayRecords.stream().filter(r -> r.getType() == 1).mapToInt(StockRecord::getNum).sum();
        long todayOut = todayRecords.stream().filter(r -> r.getType() == 2).mapToInt(StockRecord::getNum).sum();

        data.put("goodsCount", goodsCount);
        data.put("categoryCount", categoryCount);
        data.put("stockTotal", stockTotal);
        data.put("todayIn", todayIn);
        data.put("todayOut", todayOut);

        List<Goods> goodsList = goodsService.list();
        List<Category> categories = categoryService.list();
        Map<Long, String> catMap = categories.stream().collect(Collectors.toMap(Category::getId, Category::getName));

        List<Stock> stocks = stockService.list();
        Map<Long, Integer> stockMap = stocks.stream().collect(Collectors.toMap(Stock::getGoodsId, Stock::getNum, (a, b) -> a));

        Map<String, Integer> categoryDist = new LinkedHashMap<>();
        for (Goods g : goodsList) {
            String catName = catMap.getOrDefault(g.getCategoryId(), "未分类");
            Integer num = stockMap.getOrDefault(g.getId(), 0);
            categoryDist.merge(catName, num, Integer::sum);
        }
        data.put("categoryDistribution", categoryDist);

        List<Map<String, Object>> recentRecords = stockRecordService.list(
                new QueryWrapper<StockRecord>().orderByDesc("created_at").last("LIMIT 10")
        ).stream().map(r -> {
            Map<String, Object> m = new HashMap<>();
            m.put("type", r.getType());
            m.put("num", r.getNum());
            m.put("goodsId", r.getGoodsId());
            m.put("createdAt", r.getCreatedAt());
            Goods g = goodsService.getById(r.getGoodsId());
            m.put("goodsName", g != null ? g.getName() : "未知");
            return m;
        }).collect(Collectors.toList());
        data.put("recentRecords", recentRecords);

        return Result.success(data);
    }

    @GetMapping("/trend")
    public Result trend() {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        QueryWrapper<StockRecord> wrapper = new QueryWrapper<>();
        wrapper.ge("created_at", sevenDaysAgo).orderByAsc("created_at");
        List<StockRecord> records = stockRecordService.list(wrapper);

        Map<String, int[]> dayMap = new LinkedHashMap<>();
        for (int i = 6; i >= 0; i--) {
            String day = LocalDateTime.now().minusDays(i).toLocalDate().toString();
            dayMap.put(day, new int[2]);
        }

        for (StockRecord r : records) {
            String day = r.getCreatedAt().toLocalDate().toString();
            int[] arr = dayMap.get(day);
            if (arr != null) {
                if (r.getType() == 1) arr[0] += r.getNum();
                else arr[1] += r.getNum();
            }
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, int[]> entry : dayMap.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", entry.getKey());
            item.put("inCount", entry.getValue()[0]);
            item.put("outCount", entry.getValue()[1]);
            result.add(item);
        }
        return Result.success(result);
    }
}