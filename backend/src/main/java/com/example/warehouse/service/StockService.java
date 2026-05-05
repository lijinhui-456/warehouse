package com.example.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.warehouse.entity.Stock;
import com.example.warehouse.entity.StockRecord;

public interface StockService extends IService<Stock> {

    void inStock(Long goodsId, Integer num, String remark, Long operatorId);

    void outStock(Long goodsId, Integer num, String remark, Long operatorId);
}
