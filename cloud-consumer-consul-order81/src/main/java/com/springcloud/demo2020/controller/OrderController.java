package com.springcloud.demo2020.controller;

import com.springcloud.demo2020.entity.Result;
import com.springcloud.demo2020.entity.Payment;
import com.springcloud.demo2020.service.OrderClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author DJH
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class OrderController {

    @Value("${http.isInvoke}")
    private boolean type;

    /**
     * 直接用url进行接口访问
     */
    private static final String PAYMENT_URL = "http://localhost:8001/";

    /**
     * 通过注册中心访问
     */
    private static final String INVOKE_URL = "http://cloud-payment-service/";

    private final RestTemplate restTemplate;

    private final OrderClientService orderClientService;

    @PostMapping("/")
    public Result<Boolean> create(@RequestBody Payment payment) {
        if (type) {
            return restTemplate.postForObject(INVOKE_URL + "payment/", payment, Result.class);
        } else {
            return restTemplate.postForObject(PAYMENT_URL + "payment/", payment, Result.class);
        }
    }

    @GetMapping("/{id}")
    public Result<Payment> getPaymentById(@PathVariable Long id) {
        return orderClientService.getPaymentById(id);
    }

    @GetMapping("/list")
    public Result<List<Payment>> getPaymentList() {
        if (type) {
            return restTemplate.getForObject(INVOKE_URL + "payment/list", Result.class);
        } else {
            return restTemplate.getForObject(PAYMENT_URL + "payment/list", Result.class);
        }
    }
}
