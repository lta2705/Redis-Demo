package com.techie.springbootrediscache.config;

import com.techie.springbootrediscache.repository.ApiKeyRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Component

public class ApiKeyInterceptor implements HandlerInterceptor {

    private final StringRedisTemplate redisTemplate;
    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyInterceptor(StringRedisTemplate redisTemplate, ApiKeyRepository apiKeyRepository) {
        this.redisTemplate = redisTemplate;
        this.apiKeyRepository = apiKeyRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String apiKey = request.getHeader("x-api-key");

        if (apiKey == null || apiKey.isBlank()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing API Key");
            return false;
        }

        // Check Redis
        String cacheKey = "apikey:" + apiKey;
        String cached = redisTemplate.opsForValue().get(cacheKey);

        if ("valid".equals(cached)) return true;
        if ("invalid".equals(cached)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
            return false;
        }

        // Check from DB
        boolean exists = apiKeyRepository.existsByApiKeyAndIsActiveTrue(apiKey);
        redisTemplate.opsForValue().set(cacheKey, exists ? "valid" : "invalid", 10, TimeUnit.MINUTES);

        if (!exists) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid API Key");
        }

        return exists;
    }
}