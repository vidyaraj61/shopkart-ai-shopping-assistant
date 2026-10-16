package com.shopkart.model;

import java.math.BigDecimal;
import java.util.List;

public record Product (
    Long id,
    String name,
    String category,
    String brand,
    BigDecimal price,     // INR, always BigDecimal for money
    double rating,        // 0.0 - 5.0
    int stock,
    String description,
    List<String> tags
) {}
