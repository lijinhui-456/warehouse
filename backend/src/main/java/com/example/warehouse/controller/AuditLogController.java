package com.example.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.warehouse.common.Result;
import com.example.warehouse.entity.AuditLog;
import com.example.warehouse.service.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-log")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer current,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String module,
                       @RequestParam(required = false) String username) {
        Page<AuditLog> page = new Page<>(current, size);
        QueryWrapper<AuditLog> wrapper = new QueryWrapper<>();
        if (module != null && !module.isEmpty()) {
            wrapper.eq("module", module);
        }
        if (username != null && !username.isEmpty()) {
            wrapper.like("username", username);
        }
        wrapper.orderByDesc("created_at");
        return Result.success(auditLogService.page(page, wrapper));
    }
}