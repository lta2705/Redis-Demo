package com.techie.springbootrediscache.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;

    @NotBlank
    private String name;
    @Positive
    private BigDecimal price;
}
