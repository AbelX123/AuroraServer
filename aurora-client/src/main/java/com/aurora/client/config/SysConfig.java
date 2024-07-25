package com.aurora.client.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * springboot 系统级别配置
 */
@Slf4j
public class SysConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String SYS_MYSQL_HOST = "sys_mysql_host";
    private static final String SYS_MYSQL_PORT = "sys_mysql_port";
    private static final String SYS_MYSQL_DATABASE = "sys_mysql_database";
    private static final String SYS_MYSQL_USERNAME = "sys_mysql_username";
    private static final String SYS_MYSQL_PASSWORD = "sys_mysql_password";

    // 初始化
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        log.info("初始化配置源");
        // 配置数据源
        DataSource dataSource = createDataSource();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        // 从数据库中读取配置
        List<Map<String, Object>> configs = jdbcTemplate.queryForList("SELECT * FROM aurora_config");
        configs.forEach(map -> {
            String key = (String) map.get("config_key");
            String value = (String) map.get("config_value");
            System.setProperty(key, value);
        });
        log.info("初始化配置源结束");
    }

    private DataSource createDataSource() {
        String host = System.getenv(SYS_MYSQL_HOST);
        String port = System.getenv(SYS_MYSQL_PORT);
        String database = System.getenv(SYS_MYSQL_DATABASE);
        String username = System.getenv(SYS_MYSQL_USERNAME);
        String password = System.getenv(SYS_MYSQL_PASSWORD);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(String.format("jdbc:mysql://%s:%s/%s", host, port, database));
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }
}
