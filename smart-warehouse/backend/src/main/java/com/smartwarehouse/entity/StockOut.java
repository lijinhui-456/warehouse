package com.smartwarehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 出库单实体
 */
@Data
@TableName("wms_stock_out")
public class StockOut implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderNo;
    private String type;
    private Long warehouseId;
    private String warehouseName;
    private Long customerId;
    private String customerName;
    private BigDecimal totalAmount;
    private Integer itemCount;
    private Integer status;
    private String remark;
    private Integer deleted;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableField(fill = FieldFill.INSERT)
    private Long createBy;
}