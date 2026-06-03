package com.example.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.warehouse.entity.StockRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockRecordMapper extends BaseMapper<StockRecord> {
}
