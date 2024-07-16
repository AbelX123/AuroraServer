package com.aurora.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@MapperScan("com.aurora.client.mapper")
@EnableWebSocket
@SpringBootApplication
public class AuroraClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AuroraClientApplication.class, args);
        System.out.println("application");
    }
}
