package com.smartwarehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体
 */
@Data
@TableName("wms_goods")
public class Goods implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private String barcode;
    private Long categoryId;
    private String categoryName;
    private String brand;
    private String unit;
    private BigDecimal costPrice;
    private BigDecimal salePrice;
    private BigDecimal marketPrice;
    private Integer status;
    private String description;
    private String image;
    private String specs;
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}