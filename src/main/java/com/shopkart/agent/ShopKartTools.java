package com.shopkart.agent;

import com.shopkart.model.*;
import com.shopkart.repository.ProductRepository;
import com.shopkart.service.OrderService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ShopKartTools {

    private final ProductRepository products;
    private final OrderService orders;

    public ShopKartTools(ProductRepository products, OrderService orders) {
        this.products = products;
        this.orders = orders;
    }

    @Tool(description = "Search the ShopKart catalog by keyword and optional maximum price in INR. "
            + "Returns matching products with id, name, brand, price, rating and stock.")
    public List<Product> searchProducts(
            @ToolParam(description = "Keyword such as 'earbuds', 'running shoes', or a brand name")
            String query,
            @ToolParam(description = "Maximum price in INR, or null for no limit", required = false)
            BigDecimal maxPrice) {
        return products.search(query, maxPrice);
    }

    @Tool(description = "Check how many units of a product are currently in stock. "
            + "Use this before promising availability to a customer.")
    public String checkStock(
            @ToolParam(description = "The numeric product id") Long productId) {
        return products.findById(productId)
                .map(p -> p.stock() > 0
                        ? p.name() + " has " + p.stock() + " units in stock."
                        : p.name() + " is currently out of stock.")
                .orElse("No product exists with id " + productId + ".");
    }

    @Tool(description = "Place an order for a customer. Only call this after the customer has "
            + "clearly confirmed the product and quantity. Returns the order id and total in INR.")
    public String placeOrder(
            @ToolParam(description = "The customer's numeric id") Long customerId,
            @ToolParam(description = "The product id to order") Long productId,
            @ToolParam(description = "Quantity to order, must be at least 1") int quantity) {
        try {
            Order order = orders.place(customerId, List.of(new OrderItem(productId, quantity)));
            return "Order #" + order.id() + " placed successfully. Total: INR " + order.total()
                    + ". Status: " + order.status() + ".";
        } catch (RuntimeException ex) {
            return "Could not place the order: " + ex.getMessage();
        }
    }
}