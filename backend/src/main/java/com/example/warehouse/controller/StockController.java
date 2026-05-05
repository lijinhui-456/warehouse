package com.example.warehouse.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.warehouse.common.Result;
import com.example.warehouse.entity.Goods;
import com.example.warehouse.entity.Stock;
import com.example.warehouse.entity.StockRecord;
import com.example.warehouse.service.GoodsService;
import com.example.warehouse.service.StockRecordService;
import com.example.warehouse.service.StockService;
import com.example.warehouse.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/stock")
public class StockController {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockRecordService stockRecordService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private JwtUtils jwtUtils;
    @GetMapping("/list")
    public Result list() {
        List<Stock> stocks = stockService.list();
        List<Goods> goodsList = goodsService.list();
        Map<Long, String> goodsMap = goodsList.stream()
                .collect(Collectors.toMap(Goods::getId, Goods::getName));
        List<Map<String, Object>> result = stocks.stream().map(stock -> {
            Map<String, Object> item = new java.util.HashMap<>();
            item.put("id", stock.getId());
            item.put("goodsId", stock.getGoodsId());
            item.put("goodsName", goodsMap.getOrDefault(stock.getGoodsId(), "未知"));
            item.put("num", stock.getNum());
            return item;
        }).collect(Collectors.toList());
        return Result.success(result);
    }


    @PostMapping("/in")
    public Result inStock(@RequestBody Map<String, Object> params,
                          @RequestHeader("Authorization") String token) {
        Long goodsId = Long.parseLong(params.get("goodsId").toString());
        Integer num = Integer.parseInt(params.get("num").toString());
        String remark = (String) params.getOrDefault("remark", "");

        // 从token里拿用户ID
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long operatorId = jwtUtils.getUserIdFromToken(token);

        stockService.inStock(goodsId, num, remark, operatorId);
        return Result.success("入库成功");
    }

    @PostMapping("/out")
    public Result outStock(@RequestBody Map<String, Object> params,
                           @RequestHeader("Authorization") String token) {
        Long goodsId = Long.parseLong(params.get("goodsId").toString());
        Integer num = Integer.parseInt(params.get("num").toString());
        String remark = (String) params.getOrDefault("remark", "");

        // 从token里拿用户ID
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long operatorId = jwtUtils.getUserIdFromToken(token);

        stockService.outStock(goodsId, num, remark, operatorId);
        return Result.success("出库成功");
    }


    @GetMapping("/record/page")
    public Result recordPage(@RequestParam(defaultValue = "1") Integer current,
                              @RequestParam(defaultValue = "10") Integer size,
                              @RequestParam(required = false) Long goodsId,
                              @RequestParam(required = false) Integer type) {
        Page<StockRecord> page = new Page<>(current, size);
        QueryWrapper<StockRecord> wrapper = new QueryWrapper<>();
        if (goodsId != null) {
            wrapper.eq("goods_id", goodsId);
        }
        if (type != null) {
            wrapper.eq("type", type);
        }
        wrapper.orderByDesc("created_at");
        Page<StockRecord> result = stockRecordService.page(page, wrapper);

        // 补充商品名称
        List<Goods> goodsList = goodsService.list();
        Map<Long, String> goodsMap = goodsList.stream()
                .collect(Collectors.toMap(Goods::getId, Goods::getName));

        result.getRecords().forEach(record -> {
            record.setRemark(goodsMap.getOrDefault(record.getGoodsId(), "未知"));
        });

        return Result.success(result);
    }
}
