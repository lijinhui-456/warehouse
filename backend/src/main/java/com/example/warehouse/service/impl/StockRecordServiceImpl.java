package com.example.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.StockRecord;
import com.example.warehouse.mapper.StockRecordMapper;
import com.example.warehouse.service.StockRecordService;
import org.springframework.stereotype.Service;

@Service
public class StockRecordServiceImpl extends ServiceImpl<StockRecordMapper, StockRecord> implements StockRecordService {
}
