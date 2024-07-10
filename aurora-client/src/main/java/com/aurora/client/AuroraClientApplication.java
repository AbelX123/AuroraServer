package com.aurora.client;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@MapperScan("com.aurora.client.mapper")
@EnableWebSocket
public class AuroraClientApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AuroraClientApplication.class, args);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            System.out.println(beanDefinitionName);
        }
    }
}
