package com.example.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.Category;
import com.example.warehouse.mapper.CategoryMapper;
import com.example.warehouse.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Category getById(Long id) {
        if (redisTemplate != null) {
            try {
                String cacheKey = "category:" + id;
                Category cached = (Category) redisTemplate.opsForValue().get(cacheKey);
                if (cached != null) return cached;
                Category category = baseMapper.selectById(id);
                if (category != null) redisTemplate.opsForValue().set(cacheKey, category, 1, TimeUnit.HOURS);
                return category;
            } catch (Exception e) {
                log.warn("Redis不可用: {}", e.getMessage());
            }
        }
        return baseMapper.selectById(id);
    }
}
