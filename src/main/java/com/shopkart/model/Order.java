package com.shopkart.model;

import java.math.BigDecimal;
import java.util.List;

public record Order (
        Long id,
        Long customerId,
        List<OrderItem> items,
        BigDecimal total,     // computed, in INR
        String status         // PLACED, SHIPPED, DELIVERED
) {}
