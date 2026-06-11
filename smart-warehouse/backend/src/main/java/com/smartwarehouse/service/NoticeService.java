package com.smartwarehouse.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smartwarehouse.common.R;
import com.smartwarehouse.entity.SysNotice;
import com.smartwarehouse.mapper.SysNoticeMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 通知服务 - 发布 / 查询
 */
@Service
public class NoticeService extends ServiceImpl<SysNoticeMapper, SysNotice> {

    /**
     * 发布一条通知（数据库写入）
     */
    public R<Long> publish(SysNotice notice) {
        notice.setStatus(1);
        this.save(notice);
        return R.ok(notice.getId());
    }

    /**
     * 当前登录用户能看到的通知列表：
     */
    public R<Map<String, Object>> my(Long userId, int page, int size) {
        IPage<SysNotice> p = this.page(
                new Page<>(page, size),
                Wrappers.<SysNotice>lambdaQuery()
                        .eq(SysNotice::getStatus, 1)
                        .and(w -> w
                                .eq(SysNotice::getScope, "ALL")
                                .or()
                                .nested(w2 -> w2.eq(SysNotice::getScope, "USER").eq(SysNotice::getToUserId, userId))
                                .eq(SysNotice::getScope, "USER")
                                .eq(SysNotice::getToUserId, userId)
                        )
                        .orderByDesc(SysNotice::getCreateTime)
        );

        Map<String, Object> m = new HashMap<>();
        m.put("total", p.getTotal());
        m.put("list", p.getRecords());
        return R.ok(m);
    }

    /**
     * 管理员查看全部通知
     */
    public R<Map<String, Object>> listAll(int page, int size) {
        IPage<SysNotice> p = this.page(
                new Page<>(page, size),
                Wrappers.<SysNotice>lambdaQuery().orderByDesc(SysNotice::getCreateTime)
        );
        Map<String, Object> m = new HashMap<>();
        m.put("total", p.getTotal());
        m.put("list", p.getRecords());
        return R.ok(m);
    }

    /**
     * 删除一条通知（逻辑删除）
     */
    public R<Boolean> del(Long id) {
        this.removeById(id);
        return R.ok(true);
    }
}