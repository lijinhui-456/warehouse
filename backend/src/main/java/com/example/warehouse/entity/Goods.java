package com.example.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品实体类
 */
@Data
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;        // 商品名称
    private Long categoryId;    // 分类ID
    private String unit;         // 单位
    private BigDecimal price;   // 单价
    private String remark;       // 备注
}
