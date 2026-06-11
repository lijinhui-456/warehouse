package com.smartwarehouse.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartwarehouse.common.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired(required = false)
    private StringRedisTemplate redisTemplate;

    @Value("${jwt.header:Authorization}")
    private String header;

    @Value("${jwt.prefix:Bearer }")
    private String prefix;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String authHeader = request.getHeader(header);
        if (authHeader == null || !authHeader.startsWith(prefix)) {
            writeResponse(response, 401, "未登录或登录已过期");
            return false;
        }

        String token = authHeader.substring(prefix.length());
        try {
            if (!jwtUtil.validateToken(token) || jwtUtil.isExpired(token)) {
                writeResponse(response, 401, "Token无效或已过期");
                return false;
            }
        } catch (Exception e) {
            writeResponse(response, 401, "Token无效");
            return false;
        }

        String username = jwtUtil.getUsername(token);

        // Redis 降级：Redis 不可用时仅校验 JWT 本身
        if (redisTemplate != null) {
            try {
                String redisToken = redisTemplate.opsForValue().get("token:" + username);
                // 若 Redis 中存在但不匹配（可能是旧会话），仍然放行（此处宽松处理）
            } catch (Exception e) {
                System.out.println("Redis check skipped: " + e.getMessage());
            }
        }

        // 将用户信息存入请求属性
        request.setAttribute("userId", jwtUtil.getUserId(token));
        request.setAttribute("username", username);
        request.setAttribute("role", jwtUtil.getRole(token));

        return true;
    }

    private void writeResponse(HttpServletResponse response, int code, String message) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(code);
        R<Void> result = R.fail(code, message);
        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
