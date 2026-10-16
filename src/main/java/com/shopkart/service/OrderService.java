package com.shopkart.service;

import com.shopkart.model.*;
import com.shopkart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final ProductRepository products;
    private final Map<Long, Order> orders = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public OrderService(ProductRepository products) {
        this.products = products;
    }

    public Order place(Long customerId, List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item");
        }
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            Product p = products.findById(item.productId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Product not found: " + item.productId()));
            if (!products.decrementStock(p.id(), item.quantity())) {
                throw new IllegalStateException("Insufficient stock for " + p.name());
            }
            total = total.add(p.price().multiply(BigDecimal.valueOf(item.quantity())));
        }
        long id = sequence.incrementAndGet();
        Order order = new Order(id, customerId, items, total, "PLACED");
        orders.put(id, order);
        return order;
    }

    public Optional<Order> findById(Long id) {
        return Optional.ofNullable(orders.get(id));
    }
}