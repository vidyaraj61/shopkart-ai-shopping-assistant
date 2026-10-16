package com.shopkart.controller;

import com.shopkart.model.*;
import com.shopkart.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

record PlaceOrderRequest(Long customerId, List<OrderItem> items) {}

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orders;

    public OrderController(OrderService orders) {
        this.orders = orders;
    }

    // POST /api/orders
    @PostMapping
    public ResponseEntity<Order> place(@RequestBody PlaceOrderRequest request) {
        Order order = orders.place(request.customerId(), request.items());
        return ResponseEntity.status(201).body(order);
    }
}