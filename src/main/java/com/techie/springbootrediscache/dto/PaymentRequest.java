package com.techie.springbootrediscache.dto;


import lombok.Data;

@Data
public class PaymentRequest {
    private String transactionId;
    private String userId;
    private double amount;

    // constructors, getters, setters
}

