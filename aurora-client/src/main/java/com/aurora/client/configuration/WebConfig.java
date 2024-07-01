package com.aurora.client.configuration;

import com.aurora.client.interceptor.UserRequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private UserRequestInterceptor userRequestInterceptor;

    @Autowired
    public void setUserRequestInterceptor(UserRequestInterceptor userRequestInterceptor) {
        this.userRequestInterceptor = userRequestInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userRequestInterceptor).addPathPatterns("/**");
    }
}
