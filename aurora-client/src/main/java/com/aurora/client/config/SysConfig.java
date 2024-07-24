package com.aurora.client.config;

import com.aurora.client.common.entity.ConfigEntity;
import com.aurora.client.mapper.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 系统配置类
 */
@Configuration
public class SysConfig implements CommandLineRunner {

    @Autowired
    private ConfigMapper configMapper;

    /**
     * @param args 启动配置参数
     */
    @Override
    public void run(String... args) {
        // 从mysql中取出配置，写进jvm环境变量
        List<ConfigEntity> configs = configMapper.selectList(null);
        configs.forEach(c -> System.setProperty(c.getConfigKey(), c.getConfigValue()));
    }
}
