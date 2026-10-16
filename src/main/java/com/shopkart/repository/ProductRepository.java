package com.shopkart.repository;

import com.shopkart.model.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public ProductRepository() {
        seed("boAt Airdopes 141", "Audio", "boAt", "1299", 4.2, 58,
             "Wireless earbuds with 42-hour playback and low-latency mode",
             List.of("wireless", "bluetooth", "earbuds", "sports"));
        seed("Noise Buds VS104", "Audio", "Noise", "1099", 4.0, 34,
             "TWS earbuds with quad-mic ENC and fast charging",
             List.of("wireless", "bluetooth", "earbuds"));
        seed("Nike Revolution 7", "Footwear", "Nike", "3495", 4.5, 12,
             "Lightweight running shoes with soft foam cushioning",
             List.of("running", "sports", "shoes"));
        seed("Puma Softride", "Footwear", "Puma", "2999", 4.3, 0,
             "Everyday running shoes with breathable mesh upper",
             List.of("running", "sports", "shoes"));
        seed("Noise ColorFit Pulse 2", "Wearables", "Noise", "1499", 4.1, 41,
             "Smartwatch with 1.8 inch display and SpO2 tracking",
             List.of("smartwatch", "fitness", "wearable"));
        seed("boAt Rockerz 255 Pro", "Audio", "boAt", "1499", 4.4, 27,
             "Neckband with 40-hour playback and ASAP charge",
             List.of("wireless", "bluetooth", "neckband", "sports"));
    }

    private void seed(String name, String cat, String brand, String price,
                      double rating, int stock, String desc, List<String> tags) {
        long id = sequence.incrementAndGet();
        store.put(id, new Product(id, name, cat, brand,
                new BigDecimal(price), rating, stock, desc, tags));
    }

    public List<Product> findAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Product> search(String query, BigDecimal maxPrice) {
        String q = query == null ? "" : query.toLowerCase();
        return store.values().stream()
                .filter(p -> q.isBlank()
                        || p.name().toLowerCase().contains(q)
                        || p.category().toLowerCase().contains(q)
                        || p.tags().stream().anyMatch(t -> t.contains(q)))
                .filter(p -> maxPrice == null || p.price().compareTo(maxPrice) <= 0)
                .sorted(Comparator.comparingDouble(Product::rating).reversed())
                .toList();
    }

    public boolean decrementStock(Long id, int qty) {
        Product p = store.get(id);
        if (p == null || p.stock() < qty) return false;
        store.put(id, new Product(p.id(), p.name(), p.category(), p.brand(),
                p.price(), p.rating(), p.stock() - qty, p.description(), p.tags()));
        return true;
    }
}
