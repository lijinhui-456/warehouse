package com.smartwarehouse.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 通知消息生产者
 * 发布流程：管理员发布 -> DB 写入 -> MQ 发送 -> Consumer 异步消费（写操作日志/推红点等）
 *
 * 注：主业务流程一定先写 DB；MQ 仅作为"事后异步处理"的通道，不可用失败不会影响通知已发布的事实。
 */
@Component
public class NoticeMqProducer {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送通知消息到 warehouse.notice.queue
     */
    public void sendNoticePublish(Long noticeId, String title, String content, String scope) {
        NoticeMessage msg = new NoticeMessage();
        msg.setNoticeId(noticeId);
        msg.setTitle(title);
        msg.setContent(content);
        msg.setScope(scope);
        rabbitTemplate.convertAndSend(
                com.smartwarehouse.config.MqConfig.EXCHANGE,
                com.smartwarehouse.config.MqConfig.ROUTING_KEY,
                msg
        );
        System.out.println("[MQ] 通知消息已发送 noticeId=" + noticeId + " title=" + title);
    }
}
