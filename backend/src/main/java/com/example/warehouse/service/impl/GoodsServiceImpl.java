package com.example.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.warehouse.entity.Goods;
import com.example.warehouse.mapper.GoodsMapper;
import com.example.warehouse.service.GoodsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    private static final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    @Autowired(required = false)
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Goods getById(Long id) {
        if (redisTemplate != null) {
            try {
                String cacheKey = "goods:" + id;
                Goods cached = (Goods) redisTemplate.opsForValue().get(cacheKey);
                if (cached != null) return cached;
                Goods goods = baseMapper.selectById(id);
                if (goods != null) redisTemplate.opsForValue().set(cacheKey, goods, 1, TimeUnit.HOURS);
                return goods;
            } catch (Exception e) {
                log.warn("Redis不可用: {}", e.getMessage());
            }
        }
        return baseMapper.selectById(id);
    }
}
