package com.springmsproject.orderservice.controller;

import com.springmsproject.orderservice.dto.OrderRequest;
import com.springmsproject.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;


@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory" , fallbackMethod = "fallbackMethod") // inventory -> name given to circuit breaker instance
    @TimeLimiter(name="inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest){
        return CompletableFuture.supplyAsync(()->orderService.createOrder(orderRequest));
        // added completableFuture because termite will wait and try fro some hits and it's a Async call
    }

    public CompletableFuture<String>fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException){
        return CompletableFuture.supplyAsync(()->"oops! something went wrong,please order after sometime!!");
    }

}
