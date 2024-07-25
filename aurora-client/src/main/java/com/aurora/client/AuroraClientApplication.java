package com.aurora.client;

import com.aurora.client.config.SysConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@MapperScan("com.aurora.client.mapper")
@EnableWebSocket
@SpringBootApplication
public class AuroraClientApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AuroraClientApplication.class);
        application.addInitializers(new SysConfig());
        application.run(args);
    }
}
