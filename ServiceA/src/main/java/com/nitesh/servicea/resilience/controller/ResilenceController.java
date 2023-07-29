package com.nitesh.servicea.resilience.controller;

import com.nitesh.servicea.resilience.service.ProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class ResilenceController {
    private final ProductService productService;

    public ResilenceController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path = "/api/v1/upstream/ping")
    public String pingUpstream() {
        return "PONG";
    }


    // Demonstrates the use of CircuitBreaker annotation
    @GetMapping(path = "/api/v1/upstream/product/cb/{id}")
    public String getProduct(@PathVariable int id) {
        return productService.findProductWithCircuitBreaker(id, new Random().nextInt(2000));
    }

    // write a method to demonstrate the use of Retry annotation here
    @GetMapping(path = "/api/v1/upstream/product/retry/{id}")
    public String getProductWithRetry(@PathVariable int id) {
        return productService.findProductWithRetry(id);
    }

    // write a method to demonstrate the use of RateLimiter annotation here
    @GetMapping(path = "/api/v1/upstream/product/rl/{id}")
    public String getProductWithRateLimit(@PathVariable int id) {
        return productService.findProductWithRateLimit(id);
    }

    // write a method to demonstrate the use of TimeLimiter annotation here
    @GetMapping(path = "/api/v1/upstream/product/tl/{id}")
    public String getProductWithTimeLimit(@PathVariable int id) throws InterruptedException, ExecutionException {
        CompletableFuture<String> response = productService.findProductWithTimeLimit(id, new Random().nextInt(3000));
        return response.get();
    }

    @GetMapping(path = "/api/v1/upstream/product/tl2/{id}")
    public String getProductWithTimeLimit2(@PathVariable int id) {
        return productService.findProductWithTimeLimit2(id, new Random().nextInt(3000));
    }

    // write a method to demonstrate the use of Bulkhead annotation here
    @GetMapping(path = "/api/v1/upstream/product/bh/{id}")
    public String getProductWithBulkhead(@PathVariable int id) {
        return productService.findProductWithBulkHead(id);
    }

    @GetMapping(path = "/api/v1/upstream/product/all/{id}")
    public String getProductWithAllAnnotations(@PathVariable int id) throws InterruptedException, ExecutionException {
        CompletableFuture<String> response = productService.findProductWithAllAnnotations(id);
        return response.get();
    }
}
