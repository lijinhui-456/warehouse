package com.example.warehouse.controller;
import com.example.warehouse.common.Result;
import com.example.warehouse.entity.User;
import com.example.warehouse.service.UserService;
import com.example.warehouse.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.login(username, password);

        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        String token = jwtUtils.generateToken(user.getId(), user.getUsername());

        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("userId", user.getId());
        data.put("username", user.getUsername());
        data.put("realName", user.getRealName());

        return Result.success(data);
    }
    @PostMapping("/zhuce")
    public Result zhuce(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String realName = params.get("realName");
       boolean a= userService.zhuce(username,password,realName);
       if (a){
           return Result.success();
       }else {
           return Result.error("账号已存在");
       }
    }

    @GetMapping("/info")
    public Result getUserInfo(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        if (!jwtUtils.validateToken(token)) {
            return Result.error(401, "token已过期");
        }

        Long userId = jwtUtils.getUserIdFromToken(token);
        User user = userService.getById(userId);

        return Result.success(user);
    }
}
