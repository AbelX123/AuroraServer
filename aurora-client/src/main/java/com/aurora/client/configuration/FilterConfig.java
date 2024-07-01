package com.aurora.client.configuration;

import com.aurora.client.filter.UserRequestFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<UserRequestFilter> userRequestFilter() {
        FilterRegistrationBean<UserRequestFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new UserRequestFilter());
        bean.addUrlPatterns("/*");
        return bean;
    }
}
