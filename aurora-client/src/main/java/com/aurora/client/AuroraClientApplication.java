package com.aurora.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.aurora.client.mapper")
public class AuroraClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuroraClientApplication.class, args);
    }
}
