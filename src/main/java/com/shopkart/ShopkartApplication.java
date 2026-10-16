package com.shopkart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.shopkart")
public class ShopkartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopkartApplication.class, args);
    }
}