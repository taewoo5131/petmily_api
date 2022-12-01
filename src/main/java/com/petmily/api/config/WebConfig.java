package com.petmily.api.config;

import com.petmily.api.common.RedisUtil;
import com.petmily.api.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AuthInterceptor(redisUtil))
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/member/join", "/member/login", "/member/logout", "/member/refresh-token");
    }
}
