package com.techie.springbootrediscache.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techie.springbootrediscache.dto.PaymentRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000) // mỗi 2s check queue
    public void consume() {
        String json = redisTemplate.opsForList().rightPop("paymentQueue");
        if (json != null) {
            try {
                PaymentRequest req = objectMapper.readValue(json, PaymentRequest.class);
                log.info("⬇️ Consumed payment: tx={}, user={}, amount={}", req.getTransactionId(), req.getUserId(), req.getAmount());

                // Giả lập xử lý
                Thread.sleep(1000);
                log.info("Payment processed for tx={}", req.getTransactionId());

            } catch (Exception e) {
                log.error(" Failed to process: " + json, e);
            }
        } else {
            log.info("No message in queue...");
        }
    }
}
