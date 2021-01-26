package com.example.config;

import com.example.interceptor.RateLimiter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration addInterceptor = registry.addInterceptor(new RateLimiter());
        addInterceptor.excludePathPatterns("/css/**");
        addInterceptor.excludePathPatterns("/js/**");
        addInterceptor.excludePathPatterns("/bootstrap/**");
        addInterceptor.excludePathPatterns("/");
        addInterceptor.excludePathPatterns("/login");
        addInterceptor.excludePathPatterns("/loginCheck");
        addInterceptor.addPathPatterns("/**");
    }
}
