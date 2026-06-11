package com.smartwarehouse.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 系统通知 - 管理员发布、所有人可见（ALL）或指定用户可见（USER）
 */
@Data
@TableName("sys_notice")
public class SysNotice {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 标题 */
    private String title;

    /** 内容 */
    private String content;

    /** 范围：ALL=全站，USER=指定用户 */
    private String scope;

    /** 指定用户ID（scope=USER时必填） */
    private Long toUserId;

    /** 发布者ID */
    private Long publisherId;

    /** 发布者姓名 */
    private String publisherName;

    /** 状态：1=已发布，0=草稿 */
    private Integer status;

    @TableLogic
    private Integer deleted;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
