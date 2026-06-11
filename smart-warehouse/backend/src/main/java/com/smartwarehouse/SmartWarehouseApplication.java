package com.smartwarehouse;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 智能仓储物流管理系统 - 主启动类
 */
@SpringBootApplication
@MapperScan("com.smartwarehouse.mapper")
@EnableScheduling
public class SmartWarehouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartWarehouseApplication.class, args);
    }
}