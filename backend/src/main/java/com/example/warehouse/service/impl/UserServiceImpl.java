package com.example.warehouse.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.User;
import com.example.warehouse.mapper.UserMapper;
import com.example.warehouse.service.UserService;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

      private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

      @Autowired
      private UserMapper userMapper;

      @Autowired(required = false)
      private RedisTemplate<String, Object> redisTemplate;

    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = userMapper.selectOne(wrapper);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public boolean zhuce(String username, String password,String realName) {
       QueryWrapper<User> wrapper = new QueryWrapper<>();
       wrapper.eq("username", username);
       boolean exists = userMapper.exists(wrapper);
       if(exists){
           return false;
       }
       User user = new User();
       user.setUsername(username);
       user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
       user.setRealName(realName);
       user.setRole("USER");
       userMapper.insert(user);
       return true;
    }

    public User getById(Long id) {
        if (redisTemplate != null) {
            try {
                String cacheKey = "user:" + id;
                User cached = (User) redisTemplate.opsForValue().get(cacheKey);
                if (cached != null) return cached;
                User user = userMapper.selectById(id);
                if (user != null) redisTemplate.opsForValue().set(cacheKey, user, 1, TimeUnit.HOURS);
                return user;
            } catch (Exception e) {
                log.warn("Redis不可用: {}", e.getMessage());
            }
        }
        return userMapper.selectById(id);
    }

    public boolean save(User entity) { return userMapper.insert(entity) > 0; }
    public boolean updateById(User entity) { return userMapper.updateById(entity) > 0; }
    public boolean removeById(Long id) { return userMapper.deleteById(id) > 0; }
    public java.util.List<User> list() { return userMapper.selectList(null); }
}
