package com.example.warehouse.config;

import com.example.warehouse.service.AuditLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private AuditLogService auditLogService;

    @Pointcut("execution(* com.example.warehouse.controller.*.*(..)) && " +
            "!execution(* com.example.warehouse.controller.*.list*(..)) && " +
            "!execution(* com.example.warehouse.controller.*.page*(..)) && " +
            "!execution(* com.example.warehouse.controller.UserController.login(..))")
    public void operationPointcut() {}

    @Around("operationPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();

        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest request = attrs.getRequest();
                String auth = request.getHeader("Authorization");
                Long userId = null;
                String username = "未知";

                if (auth != null && auth.startsWith("Bearer ")) {
                    String token = auth.substring(7);
                    try {
                        io.jsonwebtoken.Claims claims = io.jsonwebtoken.Jwts.parserBuilder()
                                .setSigningKey(Base64.getDecoder().decode("YTJkM2Y1ZjdhOGI5YzBlMWQ0ZTZmN2E4YjljMGUxZDRlNmY3YThiOWMwZTFk"))
                                .build()
                                .parseClaimsJws(token)
                                .getBody();
                        userId = Long.parseLong(claims.get("userId").toString());
                        username = claims.get("username").toString();
                    } catch (Exception e) {
                        // token解析失败
                    }
                }

                if (userId != null) {
                    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                    String module = getModuleName(joinPoint.getTarget().getClass().getSimpleName());
                    String operation = getOperationName(request.getMethod());
                    String detail = signature.getMethod().getName() + " - " + request.getRequestURI();

                    auditLogService.log(userId, username, operation, module, detail, getIp(request));
                }
            }
        } catch (Exception ignored) {
        }

        return result;
    }

    private String getModuleName(String className) {
        if (className.contains("Goods")) return "商品管理";
        if (className.contains("Category")) return "分类管理";
        if (className.contains("Stock")) return "库存管理";
        if (className.contains("User")) return "用户管理";
        return className;
    }

    private String getOperationName(String method) {
        if ("POST".equalsIgnoreCase(method)) return "新增";
        if ("PUT".equalsIgnoreCase(method)) return "修改";
        if ("DELETE".equalsIgnoreCase(method)) return "删除";
        return "查询";
    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) ip = request.getRemoteAddr();
        return ip;
    }
}