package com.smartwarehouse.mq;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 通知消息消费者
 *  - 消费 warehouse.notice.queue 的消息
 *  - 主用途：异步写入操作日志 / 统计 / 扩展（如发短信/邮件）
 *  - 当前实现：只打印日志 + 手动 ack；如有需要可在 handle 内扩展业务
 */
@Component
public class NoticeMqConsumer {

    /**
     * 监听通知队列
     * @param msg 反序列化后的消息对象
     * @param channel rabbit channel
     * @param tag delivery tag, 用于手动 ack
     */
    @RabbitListener(queues = "warehouse.notice.queue")
    public void onNotice(NoticeMessage msg, Channel channel,
                         @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            handle(msg);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            System.err.println("[MQ] 通知消息处理失败, 原因: " + e.getMessage());
            try {
                // 异常消息退回队列（生产环境可根据业务需要选择 reject 丢弃或转死信队列）
                channel.basicNack(tag, false, true);
            } catch (Exception ignored) {
            }
        }
    }

    private void handle(NoticeMessage msg) {
        // 在控制台打印，以便开发/调试时直观看到消息被消费
        System.out.println("[MQ] 收到通知消息 noticeId=" + msg.getNoticeId()
                + " scope=" + msg.getScope() + " title=" + msg.getTitle());
        // 这里可以扩展：
        //  - 写操作日志表（sys_oper_log）
        //  - 推 websocket 给在线用户（实现实时红点）
        //  - 发邮件 / 短信通知（需要外部服务时在此调用）
    }
}
