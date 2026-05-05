package com.example.warehouse.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.User;
import com.example.warehouse.mapper.UserMapper;
import com.example.warehouse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
      @Autowired
      private UserMapper userMapper;

    public User login(String username, String password) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        wrapper.eq("password", password);
        return userMapper.selectOne(wrapper);
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
       user.setPassword(password);
       user.setRealName(realName);
       userMapper.insert(user);
       return true;
    }

    public User getById(Long id) { return userMapper.selectById(id); }
    public boolean save(User entity) { return userMapper.insert(entity) > 0; }
    public boolean updateById(User entity) { return userMapper.updateById(entity) > 0; }
    public boolean removeById(Long id) { return userMapper.deleteById(id) > 0; }
    public java.util.List<User> list() { return userMapper.selectList(null); }
}
