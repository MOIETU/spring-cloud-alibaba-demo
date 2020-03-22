package com.shaylee.apioss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ShayleeApiOssApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShayleeApiOssApplication.class, args);
    }

}
