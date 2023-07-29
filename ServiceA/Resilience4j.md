## Use cases for resilience4j :

1. Circuit Breaker
2. Ratelimiter
3. Bulkhead
4. Timelimiter
5. Retry

## Priority Order

```agsl
Retry ( CircuitBreaker ( RateLimiter ( TimeLimiter ( Bulkhead ( Function ) ) ) ) )
Where Bulkhead has the highest priority and Retry the lowest.
```

## Sample Config File:

```yaml
management:
  health:
    circuitbreakers:
      enabled: true
  endpoints:
    web:
      exposure:
        include: health
  endpoint:
    health:
      show-details: always


resilience4j:
  circuitbreaker:
    instances:
      productService:
        register-health-indicator: true
        event-consumer-buffer-size: 10
        failure-rate-threshold: 80
        minimum-number-of-calls: 5
        automatic-transition-from-open-to-half-open-enabled: true
        wait-duration-in-open-state: 20s
        permitted-number-of-calls-in-half-open-state: 3
        sliding-window-size: 10
        sliding-window-type: COUNT_BASED
  retry:
    instances:
      productService:
        max-retry-attempts: 3
        wait-duration: 10s

  timelimiter:
    instances:
      productService:
        timeout-duration: 1s
        cancel-running-future: true
  ratelimiter:
    instances:
      productService:
        limit-for-period: 5
        limit-refresh-period: 1s
        timeout-duration: 2s

  bulkhead:
    instances:
      productService:
        max-concurrent-calls: 2
        max-wait-duration: 1s
  thread-pool-bulkhead:
    instances:
      productService:
        max-thread-pool-size: 20
        core-thread-pool-size: 10
        queue-capacity: 20

```

For more details on configuration please refer to [Resilience4j](https://resilience4j.readme.io/docs/getting-started-3)