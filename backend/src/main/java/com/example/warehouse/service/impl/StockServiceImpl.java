package com.example.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.Stock;
import com.example.warehouse.entity.StockRecord;
import com.example.warehouse.mapper.StockMapper;
import com.example.warehouse.mapper.StockRecordMapper;
import com.example.warehouse.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {

    @Autowired
    private StockRecordMapper stockRecordMapper;

    /**
     * 入库
     */
    @Override
    @Transactional  // 加上事务，保证数据一致性
    public void inStock(Long goodsId, Integer num, String remark, Long operatorId) {
        // 1. 更新库存
        updateStockNum(goodsId, num);

        // 2. 记录流水
        StockRecord record = new StockRecord();
        record.setGoodsId(goodsId);
        record.setType(1);  // 1代表入库
        record.setNum(num);
        record.setRemark(remark);
        record.setOperatorId(operatorId);
        stockRecordMapper.insert(record);
    }

    /**
     * 出库
     */
    @Override
    @Transactional
    public void outStock(Long goodsId, Integer num, String remark, Long operatorId) {
        // 检查库存是否足够
        Stock stock = this.getOne(new QueryWrapper<Stock>().eq("goods_id", goodsId));
        if (stock == null || stock.getNum() < num) {
            throw new RuntimeException("库存不足！");
        }

        // 1. 更新库存（减少）
        updateStockNum(goodsId, -num);

        // 2. 记录流水
        StockRecord record = new StockRecord();
        record.setGoodsId(goodsId);
        record.setType(2);  // 2代表出库
        record.setNum(num);
        record.setRemark(remark);
        record.setOperatorId(operatorId);
        stockRecordMapper.insert(record);
    }

    /**
     * 更新库存数量（增加或减少）
     */
    private void updateStockNum(Long goodsId, Integer num) {
        Stock stock = this.getOne(new QueryWrapper<Stock>().eq("goods_id", goodsId));
        if (stock == null) {
            stock = new Stock();
            stock.setGoodsId(goodsId);
            stock.setNum(num);
            this.save(stock);
        } else {
            stock.setNum(stock.getNum() + num);
            this.updateById(stock);

        }
    }
}
