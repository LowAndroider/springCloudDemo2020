package com.springcloud.demo2020.service;

import com.springcloud.demo2020.config.FeignConfiguration;
import com.springcloud.demo2020.entity.CommonResult;
import com.springcloud.demo2020.entity.Payment;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Djh
 */
@FeignClient(value = "${payment.provider.name}/payment", configuration = FeignConfiguration.class, fallbackFactory = OrderClientService.FallBackFactory.class)
public interface OrderClientService {

    @PostMapping("/")
    CommonResult<Boolean> create(@RequestBody Payment payment);

    @GetMapping("/{id}")
    CommonResult<Payment> getPaymentById(@PathVariable("id") Long id);

    @GetMapping("/list")
    CommonResult<List<Payment>> getPaymentList();

    @Slf4j
    @Component
    class FallBackFactory implements FallbackFactory<OrderClientService> {
        @Override
        public OrderClientService create(Throwable throwable) {

            return new OrderClientService() {
                @Override
                public CommonResult<Boolean> create(Payment payment) {
                    return new CommonResult<>(444, "请稍后重试", false);
                }

                @Override
                public CommonResult<Payment> getPaymentById(Long id) {
                    Payment payment = new Payment();
                    payment.setId(id);
                    return new CommonResult<>(444, "请稍后重试", payment);
                }

                @Override
                public CommonResult<List<Payment>> getPaymentList() {
                    return new CommonResult<>(444, "请稍后重试", null);
                }
            };
        }
    }
}
