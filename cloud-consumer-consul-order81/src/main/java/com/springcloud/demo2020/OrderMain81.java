package com.springcloud.demo2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author DJH
 */
@EnableFeignClients(basePackages = {"com.springcloud.demo2020.service"})
@RibbonClients(
        @RibbonClient("${payment.provider.name}")
)
@SpringBootApplication
@EnableDiscoveryClient
public class OrderMain81 {

    public static void main(String[] args) {
        SpringApplication.run(OrderMain81.class, args);
    }
}
