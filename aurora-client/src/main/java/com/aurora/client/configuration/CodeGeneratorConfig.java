package com.aurora.client.configuration;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;
import java.util.function.Consumer;

public class CodeGeneratorConfig {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/Aurora", "root", "xuyuhuai1206.")
                .globalConfig(new Consumer<GlobalConfig.Builder>() {
                    @Override
                    public void accept(GlobalConfig.Builder builder) {
                        builder.author("Baomidou")
                                .outputDir(Paths.get(System.getProperty("user.dir")) + "/aurora-client/src/main/java")
                                .commentDate("yyyy-MM-dd");

                    }
                }).packageConfig(builder -> builder
                        .parent("com.aurora.client")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                ).strategyConfig(builder -> builder
                        .entityBuilder()
                        .enableLombok()
                ).templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
