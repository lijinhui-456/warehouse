package com.example.warehouse.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 库存变动记录实体类（入库、出库都记在这里）
 */
@Data
@TableName("stock_record")
public class StockRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long goodsId;       // 商品ID
    private Integer type;       // 类型：1入库 2出库
    private Integer num;        // 数量
    private String remark;      // 备注
    private Long operatorId;    // 操作人ID
    private LocalDateTime createdAt;
}
