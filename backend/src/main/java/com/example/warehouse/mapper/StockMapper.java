package com.example.warehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.warehouse.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}
