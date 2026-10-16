package com.shopkart.controller;

import com.shopkart.model.Product;
import com.shopkart.repository.ProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository products;

    public ProductController(ProductRepository products) {
        this.products = products;
    }

    // GET /api/products?q=earbuds&maxPrice=2000
    @GetMapping
    public List<Product> list(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) BigDecimal maxPrice) {
        if (q == null && maxPrice == null) {
            return products.findAll();
        }
        return products.search(q, maxPrice);
    }

    // GET /api/products/3
    @GetMapping("/{id}")
    public ResponseEntity<Product> byId(@PathVariable Long id) {
        return products.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}