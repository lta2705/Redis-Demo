package com.techie.springbootrediscache.repository;

import com.techie.springbootrediscache.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, Long> {
    boolean existsByApiKeyAndIsActiveTrue(String apiKey);
}