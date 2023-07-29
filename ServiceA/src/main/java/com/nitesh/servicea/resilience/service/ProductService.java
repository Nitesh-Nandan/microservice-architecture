package com.nitesh.servicea.resilience.service;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {
    private final Random random = new Random();

    @CircuitBreaker(name = "productService", fallbackMethod = "fallbackForProduct")
    public String findProductWithCircuitBreaker(int pid, int delay) {
        int pause = random.nextInt(delay);
        if (pid > 20 && pid < 30) {
            throw new RuntimeException("Product Service is down");
        }
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return "CircuitBreaker: Received Product With Id: " + pid;
    }

    public String fallbackForProduct(int pid, int delay, Throwable t) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "CircuitBreaker Fallback: Received Product With Id: " + pid;
    }

    @Retry(name = "productService", fallbackMethod = "fallbackForProductRetry")
    public String findProductWithRetry(int id) {
        System.out.println("Request for Retry Method: " + id + " date: " + new Date());
        if (id == 5) {
            throw new RuntimeException("Product Service is down");
        }
        return "Retry: Received Product With Id: " + id;
    }

    public String fallbackForProductRetry(int pid, Throwable t) {
        return "Retry Fallback: Received Product With Id: " + pid;
    }

    @TimeLimiter(name = "productService", fallbackMethod = "fallbackForProductTimeLimit")
    public CompletableFuture<String> findProductWithTimeLimit(int id, int delay) {
        return CompletableFuture.supplyAsync(() -> {
            int pause = random.nextInt(delay);
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "TimeLimiter: Received Product With Id: " + id;
        });
    }

    public CompletableFuture<String> fallbackForProductTimeLimit(int pid, int delay, Throwable t) {
        return CompletableFuture.supplyAsync(() -> {
            return "TimeLimiter Fallback: Received Product With Id: " + pid;
        });
    }

    // This method will throw exception as the return type is not completable future.
    @TimeLimiter(name = "productService", fallbackMethod = "fallbackForProductTimeLimit2")
    public String findProductWithTimeLimit2(int id, int delay) {
        int pause = random.nextInt(delay);
        try {
            Thread.sleep(pause);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Received TimeLimit Product With Id: " + id;
    }

    public String fallbackForProductTimeLimit2(int pid, int delay, Throwable t) {
        return "Fallback TimeLimit for Product With Id: " + pid;
    }

    @RateLimiter(name = "productService", fallbackMethod = "fallbackForProductRateLimit")
    public String findProductWithRateLimit(int id) {
        return "RateLimiter: Received Product With Id: " + id;
    }

    public String fallbackForProductRateLimit(int id, Throwable t) {
        return "RateLimiter Fallback: Received Product With Id: " + id;
    }

    @Bulkhead(name = "productService", fallbackMethod = "fallbackForProductBulkHead")
    public String findProductWithBulkHead(int id) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "BulkHead Response for Product With Id: " + id;
    }

    public String fallbackForProductBulkHead(int id, Throwable t) {
        return "BulkHead Fallback: Response for Product With Id: " + id;
    }

    @Retry(name = "productService", fallbackMethod = "fallBackForAll")
    @RateLimiter(name = "productService", fallbackMethod = "fallBackForAll")
    @CircuitBreaker(name = "productService", fallbackMethod = "fallBackForAll")
    @Bulkhead(name = "productService", fallbackMethod = "fallBackForAll")
    @TimeLimiter(name = "productService", fallbackMethod = "fallBackForAll")
    public CompletableFuture<String> findProductWithAllAnnotations(int id) {
        return CompletableFuture.supplyAsync(() -> {
            int pause = random.nextInt(2000);
            try {
                Thread.sleep(pause);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "TimeLimiter: Received Product With Id: " + id;
        });
    }

    public CompletableFuture<String> fallBackForAll(int id, Throwable t) {
        return CompletableFuture.supplyAsync(() -> {
            return "Fallback for All Annotations: Received Product With Id: " + id;
        });
    }
}
