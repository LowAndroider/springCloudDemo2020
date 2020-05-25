package com.springcloud.demo2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Djh
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentMain8003 {

    public static void main(String[] args) {
        SpringApplication.run(PaymentMain8003.class, args);
    }
}
