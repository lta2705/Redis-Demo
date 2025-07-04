package com.techie.springbootrediscache.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration

public class WebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    public WebConfig(ApiKeyInterceptor apiKeyInterceptor) {
        this.apiKeyInterceptor = apiKeyInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/api/**"); // áp dụng cho tất cả API
    }
}
