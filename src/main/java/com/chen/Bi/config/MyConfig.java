package com.chen.Bi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 我的配置
 *
 * @author CSY
 * @date 2023/05/29
 */
@Configuration
@MapperScan("com.chen.Bi.mapper")
@EnableTransactionManagement
public class MyConfig {
}
