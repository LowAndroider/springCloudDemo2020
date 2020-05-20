package com.springcloud.demo2020.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.fasterxml.jackson.core.JsonEncoding;
import com.springcloud.demo2020.entity.CommonResult;
import com.springcloud.demo2020.entity.Payment;
import com.springcloud.demo2020.service.PaymentService;
import io.micrometer.core.instrument.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sun.security.krb5.internal.PAData;

/**
 * @author Djh
 */
@Slf4j
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/")
    public CommonResult<Boolean> create(@RequestBody Payment payment) {
        boolean save = paymentService.save(payment);
        log.info("*****插入结果: " + save);

        return CommonResult.commonResult(save);
    }

    @GetMapping("/{id}")
    public CommonResult<Payment> getPaymentById(@PathVariable Long id) {
        Payment payment = paymentService.getById(id);
        if(payment == null) {
            log.info("*****没有id为{}的订单", id);
            return new CommonResult<>(204, "没有id为" + id + "的订单");
        } else {
            log.info("*****查询结果为: {}", payment);
            return new CommonResult<>(200, "查询成功", payment);
        }
    }
}
