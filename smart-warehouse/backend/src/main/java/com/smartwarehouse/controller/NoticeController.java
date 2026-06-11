package com.smartwarehouse.controller;

import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.SysNotice;
import com.smartwarehouse.mq.NoticeMqProducer;
import com.smartwarehouse.service.NoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通知 API
 *   POST /api/notice/publish   - 管理员发布通知（写入DB后通过MQ异步消费做扩展）
 *   GET  /api/notice/my        - 当前用户看到的通知列表
 *   GET  /api/notice/admin/list - 管理员查看所有通知
 *   DELETE /api/notice/admin/{id} - 管理员删除
 */
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    @Resource
    private NoticeMqProducer noticeMqProducer;

    /**
     * 管理员发布通知
     */
    @PostMapping("/publish")
    public R<Long> publish(@RequestBody Map<String, Object> body, HttpServletRequest req) {
        String username = (String) req.getAttribute("username");
        Long userId = toLong(req.getAttribute("userId"));

        SysNotice n = new SysNotice();
        n.setTitle((String) body.get("title"));
        n.setContent((String) body.get("content"));
        String scope = (String) body.get("scope");
        n.setScope("USER".equals(scope) ? "USER" : "ALL");
        if ("USER".equals(n.getScope())) {
            n.setToUserId(toLong(body.get("toUserId")));
        }
        n.setPublisherId(userId);
        n.setPublisherName(username);

        R<Long> result = noticeService.publish(n);

        // 写入DB成功后，通过MQ通知异步做扩展（例如给所有在线用户推红点、写日志等）
        try {
            noticeMqProducer.sendNoticePublish(n.getId(), n.getTitle(), n.getContent(), n.getScope());
        } catch (Exception e) {
            // MQ 不可用时不影响主业务，只打印日志
            System.out.println("[MQ] 通知消息发送失败(忽略): " + e.getMessage());
        }

        return result;
    }

    /**
     * 当前用户的通知列表（铃铛 / 我的消息页都用这个接口）
     */
    @GetMapping("/my")
    public R<Map<String, Object>> my(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest req) {
        Long userId = toLong(req.getAttribute("userId"));
        return noticeService.my(userId, page, size);
    }

    @GetMapping("/admin/list")
    public R<Map<String, Object>> listAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        return noticeService.listAll(page, size);
    }

    @DeleteMapping("/admin/{id}")
    public R<Boolean> del(@PathVariable Long id) {
        return noticeService.del(id);
    }

    private Long toLong(Object o) {
        if (o == null) return null;
        if (o instanceof Number) return ((Number) o).longValue();
        try { return Long.valueOf(String.valueOf(o)); } catch (Exception e) { return null; }
    }
}
