package com.smartwarehouse.controller;

import com.smartwarehouse.common.R;
import com.smartwarehouse.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        return authService.login(username, password);
    }

    @PostMapping("/register")
    public R<Void> register(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String realName = params.get("realName");
        String captchaId = params.get("captchaId");
        String captchaCode = params.get("captchaCode");
        return authService.register(username, password, realName, captchaId, captchaCode);
    }

    @PostMapping("/logout")
    public R<Void> logout(@RequestAttribute(value = "username", required = false) String username) {
        return authService.logout(username);
    }

    @GetMapping("/captcha")
    public R<Map<String, Object>> getCaptcha() {
        return authService.getCaptcha();
    }
}
