package com.aurora;

import com.aurora.client.config.SysConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@MapperScan("com.aurora.**.mapper")
@EnableWebSocket
@SpringBootApplication
public class BootstrapApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(BootstrapApplication.class);
        application.addInitializers(new SysConfig());
        application.run(args);
    }

}
