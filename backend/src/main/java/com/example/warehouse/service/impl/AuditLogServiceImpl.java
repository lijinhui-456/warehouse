package com.example.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.AuditLog;
import com.example.warehouse.mapper.AuditLogMapper;
import com.example.warehouse.service.AuditLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AuditLogServiceImpl extends ServiceImpl<AuditLogMapper, AuditLog> implements AuditLogService {

    @Override
    public void log(Long userId, String username, String operation, String module, String detail, String ip) {
        AuditLog auditLog = new AuditLog();
        auditLog.setUserId(userId);
        auditLog.setUsername(username);
        auditLog.setOperation(operation);
        auditLog.setModule(module);
        auditLog.setDetail(detail);
        auditLog.setIp(ip);
        this.save(auditLog);
    }

    @Resource
    private AuditLogMapper auditLogMapper;

    public List<AuditLog> getLogList(String module, String operator) {
        LambdaQueryWrapper<AuditLog> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(module != null && !"".equals(module), AuditLog::getModule, module)
                .like(operator != null && !"".equals(operator), AuditLog::getUsername, operator)
                .orderByDesc(AuditLog::getCreatedAt);
        return auditLogMapper.selectList(wrapper);
    }
}