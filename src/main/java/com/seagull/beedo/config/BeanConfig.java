/**
 * beedo.com Inc.
 * Copyright (c) 2018- All Rights Reserved.
 */
package com.seagull.beedo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author guosheng.huang
 * @version BeanConfig.java, v 0.1 2018年12月08日 20:48 guosheng.huang Exp $
 */
@Configuration
public class BeanConfig {
    /**
     * 任务暂存区
     */
    @Bean
    public Map schedulerMap() {
        return new ConcurrentHashMap<String, ThreadPoolTaskScheduler>();
    }
}
