package com.smartwarehouse.service;

import com.smartwarehouse.common.R;
import java.util.Map;

public interface AuthService {
    R<Map<String, Object>> login(String username, String password);
    R<Void> register(String username, String password, String realName, String captchaId, String captchaCode);
    R<Void> logout(String username);
    R<Map<String, Object>> getCaptcha();
}
