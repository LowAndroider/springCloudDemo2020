package com.springcloud.demo2020.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springcloud.demo2020.entity.Result;
import com.springcloud.demo2020.entity.Payment;
import com.springcloud.demo2020.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author Djh
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private String serverPort;

    private final PaymentService paymentService;

    @PostMapping("/")
    public Result<Boolean> create(@RequestBody Payment payment) {
        boolean save = paymentService.save(payment);
        log.info("*****插入结果: " + save);

        return Result.commonResult(save);
    }

    @GetMapping("/{id}")
    @HystrixCommand(fallbackMethod = "processHystrix")
    public Result<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        if(payment == null) {
            log.info("*****没有id为{}的订单", id);
            return new Result<>(204, "没有id为" + id + "的订单");
        } else {
            log.info("*****查询结果为: {}", payment);
            return new Result<>(200, "查询成功", payment);
        }
    }

    public Result<Object> processHystrix() {
        return new Result<>(500, "请求失败");
    }

    @GetMapping("/list")
    public Result<List<Payment>> getPaymentList() {
        List<Payment> paymentList = paymentService.list();
        return new Result<>(204, "查询成功", paymentList);
    }

    @GetMapping("/zk")
    public String paymentZK() {
        return "springCloud with zookeeper: " + serverPort + "\t" + UUID.randomUUID().toString();
    }
}
