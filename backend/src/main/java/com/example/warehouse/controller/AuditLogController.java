package com.example.warehouse.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.warehouse.common.Result;
import com.example.warehouse.entity.AuditLog;
import com.example.warehouse.service.AuditLogService;
import com.example.warehouse.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audit-log")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/page")
    public Result page(@RequestHeader("Authorization") String token,
                       @RequestParam(defaultValue = "1") Integer current,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String module,
                       @RequestParam(required = false) String username) {
        // 校验权限：只有ADMIN才能查看操作日志
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        if (!jwtUtils.validateToken(token)) {
            return Result.error(401, "token已过期");
        }
        String role = jwtUtils.getRoleFromToken(token);
        if (!"ADMIN".equals(role)) {
            return Result.error(403, "无权访问，仅管理员可查看操作日志");
        }

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