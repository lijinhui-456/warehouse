package com.example.warehouse.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.warehouse.entity.AuditLog;

import java.util.List;

public interface AuditLogService extends IService<AuditLog> {
    void log(Long userId, String username, String operation, String module, String detail, String ip);

    List<AuditLog> getLogList(String module, String operator);
}