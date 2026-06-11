package com.smartwarehouse.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smartwarehouse.entity.StockOut;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockOutMapper extends BaseMapper<StockOut> {}