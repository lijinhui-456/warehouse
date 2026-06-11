package com.smartwarehouse.mq;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 通知消息 - MQ 中传输的对象
 */
@Data
public class NoticeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 数据库中已写入的 sys_notice.id */
    private Long noticeId;

    private String title;

    private String content;

    /** ALL = 全站通知 / USER = 指定用户 */
    private String scope;

    private LocalDateTime publishTime = LocalDateTime.now();
}
