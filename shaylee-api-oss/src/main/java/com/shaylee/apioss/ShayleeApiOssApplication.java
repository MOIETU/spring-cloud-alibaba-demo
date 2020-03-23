package com.shaylee.apioss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"com.shaylee"})
public class ShayleeApiOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShayleeApiOssApplication.class, args);
    }

}
