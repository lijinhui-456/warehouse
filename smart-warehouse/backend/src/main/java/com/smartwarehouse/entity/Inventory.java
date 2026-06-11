package com.smartwarehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存实体
 */
@Data
@TableName("wms_inventory")
public class Inventory implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long goodsId;
    private String goodsCode;
    private String goodsName;
    private Long warehouseId;
    private String warehouseName;
    private BigDecimal quantity;
    private BigDecimal lockedQuantity;
    private BigDecimal availableQuantity;
    private BigDecimal minQuantity;
    private BigDecimal maxQuantity;
    private String location;
    private Integer status;
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}